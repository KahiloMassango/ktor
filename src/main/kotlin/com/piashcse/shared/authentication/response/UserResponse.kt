package com.piashcse.shared.authentication.response

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val phoneNumber: String,
)
