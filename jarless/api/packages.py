from flask import jsonify, request
from yaml import load
from yaml import Loader

from jarless.services import packages


def _get_stream_from_request(field):
    request_file = request.files[field]
    request_file.stream.seek(0)
    return request_file


def create_package(overwrite=False):

    tardata_file = _get_stream_from_request("tardata")
    yamldata_file = _get_stream_from_request("yamldata")
    yaml_as_dict = load(yamldata_file.stream, Loader=Loader)

    package = packages.create_or_update_package(
        name=yaml_as_dict.get("name", ""),
        package_def=yaml_as_dict,
        tar_file=tardata_file.stream,
        filename=tardata_file.filename,
        overwrite=overwrite,
    )
    return (jsonify(package), 201)
