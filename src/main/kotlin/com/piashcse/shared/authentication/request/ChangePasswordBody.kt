package com.piashcse.shared.authentication.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class ChangePasswordBody(
    val oldPassword: String, val newPassword: String
) {
    fun validation() {
        validate(this) {
            validate(ChangePasswordBody::oldPassword).isNotNull().isNotEmpty()
            validate(ChangePasswordBody::newPassword).isNotNull().hasSize(8)
        }
    }
}
