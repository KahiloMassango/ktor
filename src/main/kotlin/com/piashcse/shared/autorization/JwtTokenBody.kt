package com.piashcse.shared.autorization

import io.ktor.server.auth.*

data class JwtTokenBody(val userId: Int, val email: String, val userType: String)
