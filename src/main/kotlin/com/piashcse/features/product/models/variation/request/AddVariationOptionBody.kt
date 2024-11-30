package com.piashcse.features.product.models.variation.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AddVariationOptionBody(
    val variationId: Int,
    val variationOptionValue: String
) {
    fun validation() {
        validate(this) {
            validate(AddVariationOptionBody::variationId).isNotNull()
            validate(AddVariationOptionBody::variationOptionValue).isNotNull().isNotEmpty()
        }
    }
}
