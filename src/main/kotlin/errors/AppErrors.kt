package com.speachr.errors

class BadRequestException(
    message: String
) : RuntimeException(message)

class NotFoundException(message: String) : RuntimeException(message)
sealed class AppException(message: String) : RuntimeException(message)
class InvalidInputException(message: String) : AppException(message)