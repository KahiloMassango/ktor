package com.piashcse.features.product.models.product.request

import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AddProductConfigurationBody(
    val qtyStock: Int,
    val image: String?,
    val price: Double,
    val colorId: Int?,
    val sizeId: Int?
) {
    fun validation() {
        validate(this) {
            validate(AddProductConfigurationBody::qtyStock).isNotNull().isGreaterThan(0)
            validate(AddProductConfigurationBody::image).isNotNull().isNotEmpty()
            validate(AddProductConfigurationBody::price).isNotNull().isGreaterThan(0.0)
        }
    }
}