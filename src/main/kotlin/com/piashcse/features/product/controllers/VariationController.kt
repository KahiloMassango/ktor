package com.piashcse.features.product.controllers

import com.piashcse.features.product.entities.VariationEntity
import com.piashcse.features.product.entities.VariationOptionEntity
import com.piashcse.features.product.entities.VariationOptionTable
import com.piashcse.features.product.models.variation.request.AddVariationBody
import com.piashcse.features.product.models.variation.request.VariationUpdateBody
import com.piashcse.features.product.models.variation.response.VariationOptionResponse
import com.piashcse.features.product.models.variation.response.VariationResponse
import com.piashcse.features.product.repository.VariationRepository
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query

class VariationController : VariationRepository {

    override suspend fun addVariation(variation: AddVariationBody): VariationResponse = query {
        VariationEntity.new {
            name = variation.variation
        }.response()
    }

    override suspend fun getVariations(): List<VariationResponse> = query {
        VariationEntity.all().map { it.response() }
    }

    override suspend fun updateVariation(id: Int, variationUpdate: VariationUpdateBody): VariationResponse = query {
        VariationEntity.findByIdAndUpdate(id) {
            it.name = variationUpdate.variation
        }?.response() ?: throw id.notFoundException()
    }

    override suspend fun deleteVariation(id: Int): Int = query {
        VariationEntity.findById(id)?.let {
            it.delete()
            id
        } ?: throw id.notFoundException()
    }

    override suspend fun getVariationOptions(variationId: Int): List<VariationOptionResponse> = query {
        val variation = VariationEntity.findById(variationId) ?: throw variationId.notFoundException()

        VariationOptionEntity.find { VariationOptionTable.variationId eq variation.id }.map { it.response() }
    }
}