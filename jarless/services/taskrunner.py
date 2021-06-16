import logging
from datetime import datetime, timezone

from containerpy.runner import DockerRunner

from jarless.services import logs, executions
from jarless.ext.configuration import get_config_from_env
from jarless.storage import aws_storage

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

runner = DockerRunner()
config = get_config_from_env()


def _prepare_inputs(inputs, inputs_def):
    file_inputs = [
        item for item in inputs_def.keys() if inputs_def[item].get("type") == "file"
    ]
    for item in file_inputs:
        if inputs.get(item):
            inputs[item] = aws_storage.get_public_download_file_url(inputs[item])
    return inputs


def _prepare_image_file(image_path):
    return aws_storage.get_public_download_file_url(image_path)


def run_task(task_definition: dict, inputs: dict, task_id: int, secrets: str) -> bool:
    """"""
    name = task_definition["name"]
    save_output_url = f"{config.JARLESS_DSN}/api/executions/{name}/{task_id}/add/output"

    task_definition["inputs"] = _prepare_inputs(inputs, task_definition["inputs"])
    if task_definition.get("image_path"):
        task_definition["inputs"]["_image_path"] = _prepare_image_file(
            task_definition.get("image_path")
        )

    task_definition["environment"] = {
        "TASK_ID": task_id,
        "PACKAGE": name,
        "SAVE_OUTPUT_VALUES_URL": f"{save_output_url}/values?secrets={secrets}",
        "SAVE_OUTPUT_FILE_URL": f"{save_output_url}/file?secrets={secrets}",
        "SECRETS": secrets,
    }

    print("TASK DEF")
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
        runner.run_task(
            task_definition, stdout_to=save_output_to, stderr_to=save_output_to
        )
        executions.update_status(task_id, "SUCCESS")
    except Exception as error:
        executions.update_status(task_id, "FAILURE", error)

    return "OK"
