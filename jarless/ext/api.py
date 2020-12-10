from flask import Flask
import connexion
from botocore.exceptions import NoCredentialsError
from jarless.exceptions import NotFoundException, InvalidValueException


def create_api_app(version="api"):
    app = Flask(__name__)
    connexion_app = connexion.FlaskApp(__name__, specification_dir="../api/")
    connexion_app.add_api("openapi.yaml", validate_responses=True, base_path="/api")
    app = connexion_app.app

    @app.errorhandler(NotFoundException)
    def not_found_handler(error):
        return {
            "detail": str(error),
            "status": 404,
            "title": "Not Found",
        }

    @app.errorhandler(InvalidValueException)
    def invalid_value_handler(error):
        return {
            "detail": str(error),
            "status": 400,
            "title": "Bad Request",
        }

    @app.errorhandler(NoCredentialsError)
    def invalid_value_handler(error):
        return {
            "detail": str(error),
            "status": 401,
            "title": "Unauthorized",
        }

    return app
