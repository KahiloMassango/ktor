package com.piashcse.features.admin.models.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AdminRegistrationBody(
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
) {
    fun validation() {
        validate(this) {
            validate(AdminRegistrationBody::username).isNotNull().isNotEmpty()
            validate(AdminRegistrationBody::email).isNotNull().isEmail()
            validate(AdminRegistrationBody::password).isNotEmpty().hasSize(min = 8)
            validate(AdminRegistrationBody::phoneNumber).isNotNull().hasSize(min = 9)
        }
    }
}