FLASK_APP=jarless.app flask db init
FLASK_APP=jarless.app flask db migrate -m "Initial version"


FLASK_APP=jarless.app flask run --host 0.0.0.0
