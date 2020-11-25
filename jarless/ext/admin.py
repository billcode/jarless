from flask_admin import Admin
from flask_admin.contrib import sqla

from jarless.ext.database import db
from jarless.models.executions import Package, Execution, Task, Log

admin = Admin()


def init_app(app):
    admin.init_app(app)
    admin.add_view(sqla.ModelView(Package, db.session, category="Executions"))
    admin.add_view(sqla.ModelView(Execution, db.session, category="Executions"))
    admin.add_view(sqla.ModelView(Task, db.session, category="Executions"))
    admin.add_view(sqla.ModelView(Log, db.session, category="Executions"))
