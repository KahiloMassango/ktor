package com.piashcse.features.product.repository

import com.piashcse.features.product.models.category.request.AddSubCategoryBody
import com.piashcse.features.product.models.category.request.SubCategoryUpdateBody
import com.piashcse.features.product.models.category.response.SubCategoryResponse
import com.piashcse.features.product.models.variation.response.VariationResponse

interface SubCategoryRepository {

    suspend fun addSubCategory(categoryBody: AddSubCategoryBody): SubCategoryResponse
    suspend fun getSubCategoryById(subCategoryId: Int): SubCategoryResponse
    suspend fun getAllSubCategories(): List<SubCategoryResponse>
    suspend fun updateSubCategory(subCategoryId: Int, subCategory: SubCategoryUpdateBody): SubCategoryResponse
    suspend fun deleteSubCategory(subCategoryId: Int): Int

    suspend fun getSubCategoryVariations(subCategoryId: Int): List<VariationResponse>

}