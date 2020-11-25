from sqlalchemy.orm.exc import NoResultFound
from jarless.models.executions import Package
from jarless.exceptions import NotFoundException


def get_package(package_name: str) -> dict:

    try:

        package = Package.query.filter(Package.name == package_name).one()

        return {
            "id": package.id,
            "name": package.name,
            "definition": package.definition,
            "created_at": package.created_at,
        }

    except NoResultFound:
        raise PackageNotFound(package_name)


class PackageNotFound(NotFoundException):
    def __init__(self, package_name):
        super().__init__(f"Package '{package_name}' not found")
