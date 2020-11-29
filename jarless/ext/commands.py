from jarless.ext.database import db
from jarless.models.executions import Execution, Package


def list_executions():
    """List executions"""
    for item in Execution.query.all():
        print("{}".format(item.id))


def bootstrap_db():
    """Add Packages into the database"""
    pkg1 = Package(
        name="helloworld",
        definition={
            "image": "jarless.hello:latest",
            "inputs": {
                "MESSAGE": {
                    "type": "str",
                }
            },
            "outputs": {
                "RESULT": {
                    "type": "str",
                }
            },
            "script": ["sh", "-c", "python hello.py --name ${MESSAGE}"],
        },
    )
    db.session.add(pkg1)
    db.session.commit()


def init_app(app):
    app.cli.add_command(app.cli.command()(list_executions))
    app.cli.add_command(app.cli.command()(bootstrap_db))
