package com.piashcse.features.product.models.category.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AddSubCategoryBody(
    val categoryId: Int,
    val subCategoryName: String
) {
    fun validation() {
        validate(this) {
            validate(AddSubCategoryBody::categoryId).isNotNull()
            validate(AddSubCategoryBody::subCategoryName).isNotNull().isNotEmpty()
        }
    }
}