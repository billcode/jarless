from jarless.ext.database import db
from jarless.services import packages
from jarless.models.executions import Execution, Task


def create_execution(package_name, inputs, description=None):

    package = packages.get_package(package_name)
    new_execution = _add_execution(description)
    new_task = _add_execution_task(new_execution.id, package["id"], inputs)

    # todo: execute task asynchronously

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
