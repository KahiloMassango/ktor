package com.piashcse.features.admin.models.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AdminUpdateBody(
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
) {
    fun validation() {
        validate(this) {
            validate(AdminUpdateBody::username).isNotNull().isNotEmpty()
            validate(AdminUpdateBody::email).isNotNull().isNotEmpty()
            validate(AdminUpdateBody::password).isNotNull().isNotEmpty()
            validate(AdminUpdateBody::phoneNumber).isNotNull().isNotEmpty()
        }
    }
}
