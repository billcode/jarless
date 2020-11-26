from jarless.ext.database import db
from jarless.models.executions import Log


def add_log(message, task_id, created_at, level):

    new_log = Log(
        message=message,
        task_id=task_id,
        level=level,
        created_at=created_at,
    )
    db.session.add(new_log)
    db.session.commit()
    return new_log
