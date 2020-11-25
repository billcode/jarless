import os
import pytest
from sqlalchemy.orm import Session

from jarless.app import create_app
from jarless.ext.database import db


@pytest.fixture(scope="session")
def app():
    os.environ["FLASK_ENV"] = "testing"
    app = create_app()
    with app.app_context():
        yield app


@pytest.fixture(scope="function", autouse=True)
def db_session(app):
    conn = db.engine.connect()
    trans = conn.begin()

    session = Session(bind=conn)
    session.begin_nested()

    yield db.session

    trans.rollback()
    conn.close()
    db.session.remove()
