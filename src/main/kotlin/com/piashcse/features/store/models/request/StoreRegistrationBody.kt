package com.piashcse.features.store.models.request

import org.valiktor.functions.*
import org.valiktor.validate

data class StoreRegistrationBody(
    val storeName: String,
    val description: String,
    val nif: String,
    val email: String,
    val password: String,
    val logo: String,
    val banner: String?,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
) {
    fun validation() {
        validate(this) {
            validate(StoreRegistrationBody::storeName).isNotNull().isNotEmpty()
            validate(StoreRegistrationBody::description).isNotNull().isNotEmpty()
            validate(StoreRegistrationBody::nif).isNotNull().isNotEmpty()
            validate(StoreRegistrationBody::email).isNotNull().isEmail()
            validate(StoreRegistrationBody::password).isNotNull().hasSize(8)
            validate(StoreRegistrationBody::latitude).isNotNull().isNotEqualTo(0.0)
            validate(StoreRegistrationBody::longitude).isNotNull().isNotEqualTo(0.0)
            validate(StoreRegistrationBody::phoneNumber).isNotNull().isNotEmpty()
        }
    }
}