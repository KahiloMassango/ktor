package com.piashcse.features.customer.models.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class CustomerUpdateBody(
    val username: String,
    val email: String,
    val phoneNumber: String,
){
    fun validation() {
        validate(this) {
            validate(CustomerUpdateBody::username).isNotNull().isNotEmpty()
            validate(CustomerUpdateBody::email).isNotNull().isNotEmpty()
            validate(CustomerUpdateBody::phoneNumber).isNotNull().isNotEmpty()
        }
    }
}
