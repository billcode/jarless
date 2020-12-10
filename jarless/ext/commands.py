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

    pkg2 = Package(
        name="sudoku_v1",
        definition={
            "image": "sudoku:latest",
            "inputs": {"text": {"type": "str"}},
            "script": ["sh", "-c", "python solve.py --numbers=${text}"],
            "outputs": {"solution": {"type": "str"}},
        },
    )
    db.session.add(pkg2)

    pkg3 = Package(
        name="sudoku_v2",
        definition={
            "image": "sudoku:latest",
            "inputs": {"text": {"type": "str"}},
            "script": ["sh", "-c", "./run_solve.py"],
            "outputs": {"solution": {"type": "str"}, "result_txt": {"type": "file"}, "duration_ms": {"type": "int"}},
        },
    )
    db.session.add(pkg3)

    pkg4 = Package(
        name="extract_text",
        definition={
            "image": "textract:latest",
            "inputs": {"file_path": {"type": "file"}},
            "script": ["sh", "-c", "./extract_text.py"],
            "outputs": {"result_path": {"type": "file"}},
        },
    )
    db.session.add(pkg4)

    db.session.commit()


def init_app(app):
    app.cli.add_command(app.cli.command()(list_executions))
    app.cli.add_command(app.cli.command()(bootstrap_db))
