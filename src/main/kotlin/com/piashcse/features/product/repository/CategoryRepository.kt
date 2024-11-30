package com.piashcse.features.product.repository

import com.piashcse.features.product.models.category.request.AddCategoryBody
import com.piashcse.features.product.models.category.request.CategoryUpdateBody
import com.piashcse.features.product.models.category.response.CategoryResponse
import com.piashcse.features.product.models.category.response.SubCategoryResponse

interface CategoryRepository {
    suspend fun getCategoryById(categoryId: Int): CategoryResponse
    suspend fun getAllCategories(): List<CategoryResponse>

    suspend fun addCategory(categoryBody: AddCategoryBody): CategoryResponse
    suspend fun updateCategory(categoryId: Int, categoryUpdate: CategoryUpdateBody): CategoryResponse
    suspend fun deleteCategory(categoryId: Int): Int

    suspend fun getCategorySubCategories(categoryId: Int): List<SubCategoryResponse>
}