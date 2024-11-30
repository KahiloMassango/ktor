package com.piashcse.features.product.models.product.request

import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AddProductBody(
    val title: String,
    val description: String,
    val isAvailable: Boolean,
    val image: String,
    val categoryId: Int,
    val subCategoryId: Int,
    val variations: List<AddProductConfigurationBody>,
) {
    fun validation() {
        validate(this) {
            validate(AddProductBody::categoryId).isNotNull()
            validate(AddProductBody::subCategoryId).isNotNull()
            validate(AddProductBody::title).isNotNull().isNotEmpty()
            validate(AddProductBody::description).isNotBlank()
            validate(AddProductBody::image).isNotNull().isNotEmpty()
        }
    }
}
