from jarless.ext.database import db
from jarless.models.executions import Execution


def list_executions():
    """List executions"""
    for item in Execution.query.all():
        print("{}".format(item.id))


def init_app(app):
    app.cli.add_command(app.cli.command()(list_executions))
