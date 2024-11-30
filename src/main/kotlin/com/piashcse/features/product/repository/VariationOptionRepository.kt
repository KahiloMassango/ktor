package com.piashcse.features.product.repository

import com.piashcse.features.product.models.variation.request.AddVariationOptionBody
import com.piashcse.features.product.models.variation.request.VariationOptionUpdateBody
import com.piashcse.features.product.models.variation.response.VariationOptionResponse

interface VariationOptionRepository {
    suspend fun addVariationOption(variationOption: AddVariationOptionBody): VariationOptionResponse
    suspend fun getVariationOptions(): List<VariationOptionResponse>
    suspend fun updateVariationOption(id: Int, variationUpdate: VariationOptionUpdateBody): VariationOptionResponse
    suspend fun deleteVariationOption(id: Int): Int
}