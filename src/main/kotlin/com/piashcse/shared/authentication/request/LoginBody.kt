package com.piashcse.shared.authentication.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isIn
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class LoginBody(
    val email: String,
    val password: String,
){
    fun validation() {
        validate(this) {
            validate(LoginBody::email).isNotNull().isEmail()
            validate(LoginBody::password).isNotNull().hasSize(min = 8)
        }
    }

}
