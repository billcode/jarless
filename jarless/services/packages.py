from sqlalchemy.orm.exc import NoResultFound
from jarless.ext.database import db
from jarless.models.executions import Package
from jarless.exceptions import NotFoundException, ConflictException
from jarless.ext.configuration import get_config_from_env
from jarless.storage import aws_storage

config = get_config_from_env()


def get_package(package_name: str = None, package_id: int = None) -> dict:

    try:

        qs = Package.query.filter(Package.name == package_name)
        if package_id:
            qs = Package.query.filter(Package.id == package_id)

        package = qs.one()
        definition = package.definition
        definition["name"] = package.name

        return {
            "id": package.id,
            "name": package.name,
            "definition": definition,
            "created_at": package.created_at,
        }

    except NoResultFound:
        raise PackageNotFound(package_name)


def create_or_update_package(name, package_def, filename, tar_file, overwrite=False):

    package = Package.query.filter(Package.name == name).one_or_none()
    if package and not overwrite:
        raise PackageConflict(name)

    if not package:
        package = Package(
            name=name,
            definition=package_def,
        )

    target_file = f"s3://{config.STORAGE_BUCKET}/packages/{name}/{filename}"
    aws_storage.upload_fileobj(tar_file, target_file)
    package_def["image_path"] = target_file

    db.session.add(package)
    db.session.commit()
    return package.to_dict()


def get_package_image_url(package_name):
    try:

        package = Package.query.filter(Package.name == package_name).one()

        image_path = package.definition.get("image_path")
        if not image_path:
            raise PackageImageNotFound(package_name)

        return {
            "url": aws_storage.get_public_download_file_url(image_path),
        }

    except NoResultFound:
        raise PackageNotFound(package_name)


class PackageNotFound(NotFoundException):
    def __init__(self, package_name):
        super().__init__(f"Package '{package_name}' not found")


class PackageImageNotFound(NotFoundException):
    def __init__(self, package_name):
        super().__init__(f"Image not found for Package '{package_name}'")


class PackageConflict(ConflictException):
    def __init__(self, package_name):
        super().__init__(f"Package '{package_name}' already exists")
