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