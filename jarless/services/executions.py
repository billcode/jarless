from multiprocessing import Process
from sqlalchemy.orm.exc import NoResultFound

from jarless.ext.database import db
from jarless.exceptions import NotFoundException
from jarless.services import packages, taskrunner
from jarless.models.executions import Execution, Task


def create_execution(package_name, inputs, description=None):

    package = packages.get_package(package_name=package_name)
    new_execution = _add_execution(description)
    new_task = _add_execution_task(new_execution.id, package["id"], inputs)

    # todo: execute task asynchronously
    _run_task_async(new_task)

    return {"id": new_execution.id, "status": new_execution.status}


def _add_execution(description=None):

    execution = Execution(
        status=Execution.STATUS_QUEUED,
        description=description,
    )
    db.session.add(execution)
    db.session.commit()
    return execution


def _add_execution_task(execution_id, package_id, inputs):

    task = Task(
        execution_id=execution_id,
        package_id=package_id,
        inputs=inputs,
    )
    db.session.add(task)
    db.session.commit()
    return task


def _run_task_async(task):

    package = packages.get_package(package_id=task.package_id)

    params = (
        package["definition"],
        task.inputs,
        task.id,
    )
    async_process = Process(
        target=taskrunner.run_task,
        args=params,
        daemon=True,
    )
    async_process.start()


def update_status(task_id, status, error=None):

    # TODO: It makes sense to use Task.status instead of Execution.status
    task = _get_task(task_id)
    execution = _get_execution(task.execution_id)

    execution.status = status
    if error:
        task.error = str(error)

    db.session.add(execution)
    db.session.add(task)
    db.session.commit()
    return execution


def _get_execution(execution_id):
    try:

        return Execution.query.filter(Execution.id == execution_id).one()

    except NoResultFound:
        raise ExecutionNotFound(execution_id)


def _get_task(task_id):
    try:

        return Task.query.filter(Task.id == task_id).one()

    except NoResultFound:
        raise TaskNotFound(task_id)


class ExecutionNotFound(NotFoundException):
    def __init__(self, execution_id):
        super().__init__(f"Execution ID '{execution_id}' not found")


class TaskNotFound(NotFoundException):
    def __init__(self, task_id):
        super().__init__(f"Task ID '{task_id}' not found")
