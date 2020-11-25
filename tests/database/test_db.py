def test_database(db_session):
    result = db_session.execute("SELECT 40 + 2")
    assert list(result)[0][0] == 42
