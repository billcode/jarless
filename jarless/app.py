from jarless.ext import configuration, api


def minimal_app(**config):
    app = api.create_api_app()
    configuration.init_app(app, **config)
    return app


def create_app(**config):
    app = minimal_app(**config)
    configuration.load_extensions(app)
    return app
