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


def add_output_value(package, task_id, secrets):
    inputs = request.get_json()
    task = executions.add_output_value(package, task_id, secrets, inputs.get("name"), inputs.get("value"))
    # TODO: add output files
    return (jsonify(task), 201)


def add_output_file(package, task_id, secrets):

    uploaded_file = request.files["filedata"]
    output_name = request.form["name"]
    uploaded_file.stream.seek(0)

    task = executions.add_output_file(
        package, task_id, secrets, output_name, uploaded_file.filename, uploaded_file.stream
    )

    # aws_storage.upload_fileobj(uploaded_file.stream, "s3://jarless/uploads/{}".format(uploaded_file.filename))
    return (jsonify(task), 201)


def get_execution(id):
    return (jsonify(executions.get_execution(id)), 200)
