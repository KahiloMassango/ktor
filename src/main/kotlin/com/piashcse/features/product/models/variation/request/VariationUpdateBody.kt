package com.piashcse.features.product.models.variation.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class VariationUpdateBody(
    val variation: String,
) {
    fun validation() {
        validate(this) {
            validate(VariationUpdateBody::variation).isNotNull().isNotEmpty()
        }
    }
}
