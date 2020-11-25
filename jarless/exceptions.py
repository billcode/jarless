class NotFoundException(RuntimeError):
    def __init__(self, message="Not found."):
        super().__init__(message)
