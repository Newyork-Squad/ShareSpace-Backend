package com.newyork.sharespace.config.exceptionHandling

import org.springframework.http.HttpStatus

/**
 * Base Exception for all custom exceptions
 */
sealed class BaseException(
    message: String,
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : RuntimeException(message)

/**
 * Resource Not Found Exception (404)
 */
class ResourceNotFoundException(
    message: String = "Resource not found"
) : BaseException(message, HttpStatus.NOT_FOUND)

/**
 * Bad Request Exception (400)
 */
class BadRequestException(
    message: String = "Bad request"
) : BaseException(message, HttpStatus.BAD_REQUEST)

/**
 * Unauthorized Exception (401)
 */
class UnauthorizedException(
    message: String = "Unauthorized access"
) : BaseException(message, HttpStatus.UNAUTHORIZED)

/**
 * Forbidden Exception (403)
 */
class ForbiddenException(
    message: String = "Access forbidden"
) : BaseException(message, HttpStatus.FORBIDDEN)

/**
 * Conflict Exception (409)
 */
class ConflictException(
    message: String = "Resource conflict"
) : BaseException(message, HttpStatus.CONFLICT)

/**
 * Validation Exception (422)
 */
class ValidationException(
    message: String = "Validation failed",
    val errors: List<String> = emptyList()
) : BaseException(message, HttpStatus.UNPROCESSABLE_ENTITY)