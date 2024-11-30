package com.piashcse.features.product.models.variation.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class VariationOptionUpdateBody(
    val variationOptionValue: String,
) {
    fun validation() {
        validate(this) {
            validate(VariationOptionUpdateBody::variationOptionValue).isNotNull().isNotEmpty()
        }
    }
}
