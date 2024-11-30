package com.piashcse.features.product.models.category.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class CategoryUpdateBody(
    val category: String
) {
    fun validation() {
        validate(this) {
            validate(CategoryUpdateBody::category).isNotNull().isNotEmpty()
        }
    }
}
