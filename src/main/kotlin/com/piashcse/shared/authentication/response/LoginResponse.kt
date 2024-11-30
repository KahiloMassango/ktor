package com.piashcse.shared.authentication.response


data class LoginResponse<T>(val user: T, val accessToken: String)

