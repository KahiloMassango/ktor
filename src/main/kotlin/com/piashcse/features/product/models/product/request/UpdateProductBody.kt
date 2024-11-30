package com.piashcse.features.product.models.product.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class UpdateProductBody(
    val categoryId: Int,
    val subCategoryId: Int,
    val productName: String,
    val description: String,
    val isAvailable: Boolean,
    val image: String,
) {
    fun validation() {
        validate(this) {
            validate(UpdateProductBody::categoryId).isNotNull()
            validate(UpdateProductBody::subCategoryId).isNotNull()
            validate(UpdateProductBody::productName).isNotNull().isNotEmpty()
            validate(UpdateProductBody::isAvailable).isNotNull()
        }
    }
}
