package com.piashcse.features.product.models.category.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class SubCategoryUpdateBody(
    val subCategoryName: String
) {
    fun validation() {
        validate(this) {
            validate(SubCategoryUpdateBody::subCategoryName).isNotNull().isNotEmpty()
        }
    }
}
