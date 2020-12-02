from flask import jsonify, request
from jarless.services import executions


def create_execution(package):
    inputs = request.get_json()

    new_execution = executions.create_execution(package, inputs)

    return (
        jsonify(
            {
                "execution": new_execution,
            }
        ),
        201,
    )


def add_output(package, task_id, secrets):
    inputs = request.get_json()
    task = executions.add_output_value(package, task_id, secrets, inputs.get("name"), inputs.get("value"))
    # TODO: add output files
    return (jsonify(task), 201)
