package com.piashcse.features.product.controllers

import com.piashcse.features.product.entities.VariationEntity
import com.piashcse.features.product.entities.VariationOptionEntity
import com.piashcse.features.product.models.variation.request.AddVariationOptionBody
import com.piashcse.features.product.models.variation.request.VariationOptionUpdateBody
import com.piashcse.features.product.models.variation.response.VariationOptionResponse
import com.piashcse.features.product.repository.VariationOptionRepository
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query

class VariationOptionController : VariationOptionRepository {

    override suspend fun addVariationOption(variationOption: AddVariationOptionBody): VariationOptionResponse = query {
        val variation = VariationEntity.findById(variationOption.variationId)
            ?: throw variationOption.variationId.notFoundException()

        VariationOptionEntity.new {
            value = variationOption.variationOptionValue
            variationId = variation.id
        }.response()
    }

    override suspend fun getVariationOptions(): List<VariationOptionResponse> = query {
        VariationOptionEntity.all().map { it.response() }
    }

    override suspend fun updateVariationOption(
        id: Int,
        variationUpdate: VariationOptionUpdateBody
    ): VariationOptionResponse = query {

        VariationOptionEntity.findByIdAndUpdate(id) {
            it.value = variationUpdate.variationOptionValue
        }?.response() ?: throw id.notFoundException()

    }

    override suspend fun deleteVariationOption(id: Int): Int = query {
        VariationOptionEntity.findById(id)?.let {
            it.delete()
            id
        } ?: throw id.notFoundException()
    }
}