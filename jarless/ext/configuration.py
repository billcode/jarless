import os
from importlib import import_module

EXTENSIONS = [
    "jarless.ext.database",
    "jarless.ext.commands",
    "jarless.ext.admin",
]


def init_app(app):
    config = get_config_from_env()
    app.config.from_object(config)


def get_config_from_env():
    # Available environments: devevelopment/testing/production
    envname = os.getenv("FLASK_ENV", "development").lower()
    if envname == "production":
        return ProductionConfig()
    elif envname == "testing":
        return TestingConfig()
    return DevelopmentConfig()


class ProductionConfig(object):
    FLASK_ENV = "production"
    DEBUG = False
    TESTING = False

    SECRET_KEY = os.getenv("FLASK_SECRET_KEY", "Ch@nG3_th1s_IN_PR0D!")
    JARLESS_DSN = "http://192.168.15.18:5000"

    # Database
    SQLALCHEMY_DATABASE_URI = os.getenv("DATABASE_URI", "postgresql://jarless:jarless@localhost/jarless")
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    # ext.admin
    FLASK_ADMIN_SWATCH = "flatly"

    # STORAGE
    STORAGE_DSN = os.getenv("STORAGE_DSN", "http://192.168.15.18:9000")
    STORAGE_BUCKET = os.getenv("STORAGE_BUCKET", "jarless")


class DevelopmentConfig(ProductionConfig):
    FLASK_ENV = "development"
    DEBUG = True


class TestingConfig(ProductionConfig):
    FLASK_ENV = "testing"
    TESTING = True
    SQLALCHEMY_DATABASE_URI = "postgresql://jarless:jarless@localhost/jarless_test"


def load_extensions(app):
    for extension in EXTENSIONS:
        mod = import_module(extension)
        print("  ...loading {}".format(extension))
        mod.init_app(app)
