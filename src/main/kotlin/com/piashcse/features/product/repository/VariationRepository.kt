package com.piashcse.features.product.repository

import com.piashcse.features.product.models.variation.request.AddVariationBody
import com.piashcse.features.product.models.variation.request.VariationUpdateBody
import com.piashcse.features.product.models.variation.response.VariationOptionResponse
import com.piashcse.features.product.models.variation.response.VariationResponse

interface VariationRepository {
    suspend fun addVariation(variation: AddVariationBody): VariationResponse
    suspend fun getVariations(): List<VariationResponse>
    suspend fun updateVariation(id: Int, variationUpdate: VariationUpdateBody): VariationResponse
    suspend fun deleteVariation(id: Int): Int

    suspend fun getVariationOptions(variationId: Int): List<VariationOptionResponse>
}