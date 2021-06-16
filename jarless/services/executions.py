import uuid
from datetime import datetime, timezone
from multiprocessing import Process
from sqlalchemy.orm.exc import NoResultFound

from jarless.ext.database import db
from jarless.exceptions import NotFoundException, InvalidValueException
from jarless.services import packages, taskrunner
from jarless.models.executions import Execution, Task
from jarless.ext.configuration import get_config_from_env
from jarless.storage import aws_storage

config = get_config_from_env()


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
        secrets=uuid.uuid4().hex,
    )
    db.session.add(task)
    db.session.commit()
    return task


def _run_task_async(task):

    # TODO: It needs to get the package again
    package = packages.get_package(package_id=task.package_id)

    params = (
        package["definition"],
        task.inputs,
        task.id,
        task.secrets,
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

    if status == "STARTED":
        task.started_at = datetime.now(timezone.utc)
    elif status in ["SUCCESS", "FAILURE"]:
        task.finished_at = datetime.now(timezone.utc)

    db.session.add(execution)
    db.session.add(task)
    db.session.commit()
    return execution


def _get_execution(execution_id):
    try:

        return Execution.query.filter(Execution.id == execution_id).one()

    except NoResultFound:
        raise ExecutionNotFound(execution_id)


def get_execution(execution_id):
    execution = _get_execution(execution_id)
    task = Task.query.filter(Task.execution_id == execution_id).one()

    return {
        "id": execution.id,
        "status": execution.status,
        "description": execution.description,
        "created_at": execution.created_at,
        "started_at": execution.started_at,
        "finished_at": execution.finished_at,
        "task": task.to_dict(),
    }


def _get_task(task_id):
    try:

        return Task.query.filter(Task.id == task_id).one()

    except NoResultFound:
        raise TaskNotFound(task_id)


def _get_and_validated_package_and_task(package_name, task_id, secrets):
    package = packages.get_package(package_name=package_name)
    task = _get_task(task_id)

    if task.secrets != secrets:
        raise RuntimeError("Not authorized secrets for add task output")

    return (package, task)


def add_output_value(package_name: str, task_id: str, secrets: str, values: dict):
    _, task = _get_and_validated_package_and_task(package_name, task_id, secrets)

    # TODO: add select for update instead
    if task.outputs is None:
        task.outputs = {}

    outputs = task.outputs.copy()
    # TODO: validate when the output is not inside the definition
    # package_outputs = package["definition"].get("outputs", {})
    # if not package_outputs.get(output):
    #    raise InvalidValueException(
    #        f"Invalid output '{output}'. Package '{package['name']}' available outputs: {list(package_outputs.keys())}"
    #    )

    for key in values.keys():
        outputs[key] = values[key]

    task.outputs = outputs

    db.session.add(task)
    db.session.commit()
    return task.to_dict()


def add_output_file(
    package_name: str, task_id: str, secrets: str, name: str, filename: str, fileobj
):
    _, task = _get_and_validated_package_and_task(package_name, task_id, secrets, name)

    # TODO: add select for update instead
    if task.outputs is None:
        task.outputs = {}

    target_file = (
        f"s3://{config.STORAGE_BUCKET}/{task.execution_id}/{task_id}/outputs/{filename}"
    )
    aws_storage.upload_fileobj(fileobj, target_file)

    outputs = task.outputs.copy()
    outputs[name] = target_file
    task.outputs = outputs

    db.session.add(task)
    db.session.commit()
    return task.to_dict()


class ExecutionNotFound(NotFoundException):
    def __init__(self, execution_id):
        super().__init__(f"Execution ID '{execution_id}' not found")


class TaskNotFound(NotFoundException):
    def __init__(self, task_id):
        super().__init__(f"Task ID '{task_id}' not found")
