package com.piashcse.features.customer.models.request

import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class CostumerRegistrationBody(
    val username: String,
    val email: String,
    val password: String,
    val profileImage: String?,
    val gender: String,
    val phoneNumber: String,
){
    fun validation() {
        validate(this) {
            validate(CostumerRegistrationBody::username).isNotNull().isNotEmpty()
            validate(CostumerRegistrationBody::email).isNotNull().isNotEmpty()
            validate(CostumerRegistrationBody::password).isNotNull().isNotEmpty().hasSize(8)
            validate(CostumerRegistrationBody::profileImage).isNotNull().isNotEmpty()
            validate(CostumerRegistrationBody::gender).isNotNull().isNotEmpty()
            validate(CostumerRegistrationBody::phoneNumber).isNotNull().isNotEmpty()
        }
    }
}
