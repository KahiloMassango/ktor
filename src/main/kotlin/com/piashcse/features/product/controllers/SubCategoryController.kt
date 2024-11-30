package com.piashcse.features.product.controllers

import com.piashcse.features.product.entities.*
import com.piashcse.features.product.models.category.request.AddSubCategoryBody
import com.piashcse.features.product.models.category.request.SubCategoryUpdateBody
import com.piashcse.features.product.models.category.response.SubCategoryResponse
import com.piashcse.features.product.models.variation.response.VariationResponse
import com.piashcse.features.product.repository.SubCategoryRepository
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query

class SubCategoryController : SubCategoryRepository {

    override suspend fun addSubCategory(categoryBody: AddSubCategoryBody): SubCategoryResponse = query {
        val category = CategoryEntity.findById(categoryBody.categoryId)
            ?: throw categoryBody.categoryId.notFoundException()

        SubCategoryEntity.new {
            this.categoryId = category.id
            name = categoryBody.subCategoryName
        }.response()
    }

    override suspend fun getSubCategoryById(subCategoryId: Int): SubCategoryResponse = query {
        SubCategoryEntity.findById(subCategoryId)?.response() ?: throw subCategoryId.notFoundException()
    }

    override suspend fun getAllSubCategories(): List<SubCategoryResponse> = query {
        SubCategoryEntity.all().map { it.response() }
    }

    override suspend fun updateSubCategory(
        subCategoryId: Int,
        subCategory: SubCategoryUpdateBody
    ): SubCategoryResponse = query {
        SubCategoryEntity.findById(subCategoryId)?.let {
            it.name = subCategory.subCategoryName
            it.response()
        } ?: throw subCategoryId.notFoundException()
    }

    override suspend fun deleteSubCategory(subCategoryId: Int): Int = query {
        SubCategoryEntity.findById(subCategoryId)?.let {
            it.delete()
            subCategoryId
        } ?: throw subCategoryId.notFoundException()
    }

    override suspend fun getSubCategoryVariations(subCategoryId: Int): List<VariationResponse> = query {
        SubCategoryEntity.find { SubCategoryTable.id eq subCategoryId }.singleOrNull()
            ?: throw subCategoryId.notFoundException()

        VariationEntity.find { VariationTable.subCategoryId eq subCategoryId }.map { it.response() }
    }
}