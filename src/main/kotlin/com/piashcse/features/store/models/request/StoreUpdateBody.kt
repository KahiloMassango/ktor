package com.piashcse.features.store.models.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotEqualTo
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class StoreUpdateBody(
    val storeName: String,
    val description: String,
    val email: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,

) {
    fun validation() {
        validate(this) {
            validate(StoreUpdateBody::storeName).isNotNull().isNotEmpty()
            validate(StoreUpdateBody::description).isNotNull().isNotEmpty()
            validate(StoreUpdateBody::email).isNotNull().isNotEmpty()
            validate(StoreUpdateBody::latitude).isNotNull().isNotEqualTo(0.0)
            validate(StoreUpdateBody::longitude).isNotNull().isNotEqualTo(0.0)
            validate(StoreUpdateBody::phoneNumber).isNotNull().isNotEmpty()
        }
    }
}
