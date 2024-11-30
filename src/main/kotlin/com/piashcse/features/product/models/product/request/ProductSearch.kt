package com.piashcse.features.product.models.product.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isNotZero
import org.valiktor.validate

data class ProductSearch(
    val limit: Int?,
    val productName: String,
    val category: String? = null,
    val subCategory: String? = null

) {
    fun validation() {
        validate(this) {
            validate(ProductSearch::limit).isNotNull().isNotZero()
            validate(ProductSearch::productName).isNotNull().isNotEmpty()
        }
    }
}
