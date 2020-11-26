import logging
from datetime import datetime, timezone

from containerpy.runner import DockerRunner

from jarless.services import logs, executions

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

runner = DockerRunner()


def run_task(task_definition: dict, inputs: dict, task_id: int) -> bool:
    """"""
    task_definition["inputs"] = inputs
    task_definition["environment"] = {
        "TASK_ID": task_id,
    }

    print(task_definition)

    def save_output_to(message, task_id=task_id):
        logs.add_log(message.decode("utf-8"), task_id, level="INFO", created_at=datetime.now(timezone.utc))
        logger.info(message)

    try:
        executions.update_status(task_id, "STARTED")
        runner.run_task(task_definition, stdout_to=save_output_to, stderr_to=save_output_to)
        executions.update_status(task_id, "SUCCESS")
    except Exception as error:
        executions.update_status(task_id, "FAILURE", error)

    return "OK"
