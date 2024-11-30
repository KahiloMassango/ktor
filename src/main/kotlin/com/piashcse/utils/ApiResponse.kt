package com.piashcse.utils

import io.ktor.http.*

data class Response<T>(
    val isSuccess: Boolean,
    val statusCode: Int,
    val data: T,
)


object ApiResponse {
    fun <T> success(data: T, statsCode: HttpStatusCode) = Response(true, data = data, statusCode = statsCode.value)
    fun <T> failure(error: T, statsCode: HttpStatusCode) = Response(false, data = error, statusCode = statsCode.value)
}
