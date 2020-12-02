class NotFoundException(RuntimeError):
    def __init__(self, message="Not found."):
        super().__init__(message)


class InvalidValueException(RuntimeError):
    def __init__(self, message="Invalid value."):
        super().__init__(message)
