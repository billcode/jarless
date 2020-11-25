def test_should_get_the_api_version(client):

    response = client.get("/api/version", headers={"content-type": "application/json"})

    assert response.status_code == 200
