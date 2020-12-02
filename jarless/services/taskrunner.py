import logging
from datetime import datetime, timezone

from containerpy.runner import DockerRunner

from jarless.services import logs, executions
from jarless.ext.configuration import get_config_from_env

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

runner = DockerRunner()
config = get_config_from_env()


def run_task(task_definition: dict, inputs: dict, task_id: int, secrets: str) -> bool:
    """"""
    name = task_definition["name"]
    save_output_url = f"{config.JARLESS_DSN}/api/executions/{name}/{task_id}/add/output"

    task_definition["inputs"] = inputs
    task_definition["environment"] = {
        "TASK_ID": task_id,
        "SAVE_OUTPUT_URL": save_output_url,
        "SECRETS": secrets,
    }

    print(task_definition)

    def save_output_to(message, task_id=task_id):
        logs.add_log(
            message.decode("utf-8"),
            task_id,
            level="INFO",
            created_at=datetime.now(timezone.utc),
        )
        logger.info(message)

    try:
        executions.update_status(task_id, "STARTED")
        runner.run_task(task_definition, stdout_to=save_output_to, stderr_to=save_output_to)
        executions.update_status(task_id, "SUCCESS")
    except Exception as error:
        executions.update_status(task_id, "FAILURE", error)

    return "OK"
