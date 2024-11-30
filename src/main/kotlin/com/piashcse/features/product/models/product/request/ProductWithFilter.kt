package com.piashcse.features.product.models.product.request

import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isNotZero
import org.valiktor.validate

data class SearchProductFilter(
    val limit: Int,
    val offset: Long,
    val productName: String,
    val maxPrice: Double?,
    val minPrice: Double?,
    val categoryId: String?,
    val subCategoryId: String?,
) {
    fun validation() {
        validate(this) {
            validate(SearchProductFilter::limit).isNotNull().isNotZero()
            validate(SearchProductFilter::offset).isNotNull()
            validate(SearchProductFilter::productName).isNotNull().isNotEmpty()
        }
    }
}
