package com.piashcse.features.product.controllers

import com.piashcse.features.product.entities.CategoryEntity
import com.piashcse.features.product.entities.CategoryTable
import com.piashcse.features.product.entities.SubCategoryEntity
import com.piashcse.features.product.entities.SubCategoryTable
import com.piashcse.features.product.models.category.request.AddCategoryBody
import com.piashcse.features.product.models.category.request.CategoryUpdateBody
import com.piashcse.features.product.models.category.response.CategoryResponse
import com.piashcse.features.product.models.category.response.SubCategoryResponse
import com.piashcse.features.product.repository.CategoryRepository
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query

class CategoryController : CategoryRepository {

    override suspend fun addCategory(categoryBody: AddCategoryBody): CategoryResponse = query {
        val categoryEntity = CategoryEntity.new {
            name = categoryBody.category
        }
        categoryEntity.response()
    }

    override suspend fun getCategoryById(categoryId: Int): CategoryResponse = query {
        CategoryEntity.find { CategoryTable.id eq categoryId }
            .toList()
            .singleOrNull()?.response() ?: throw categoryId.notFoundException()
    }

    override suspend fun getAllCategories(): List<CategoryResponse> = query {
        CategoryEntity.all().map { it.response() }
    }

    override suspend fun updateCategory(categoryId: Int, categoryUpdate: CategoryUpdateBody): CategoryResponse = query {
        CategoryEntity.findById(categoryId)?.let {
            it.name = categoryUpdate.category
            it.response()
        } ?: throw categoryId.notFoundException()
    }

    override suspend fun deleteCategory(categoryId: Int): Int = query {
        CategoryEntity.findById(categoryId)?.let {
                it.delete()
                categoryId
            } ?: throw categoryId.notFoundException()
    }

    override suspend fun getCategorySubCategories(categoryId: Int): List<SubCategoryResponse> = query {
        CategoryEntity.findById(categoryId) ?: throw categoryId.notFoundException()

        SubCategoryEntity.find { SubCategoryTable.categoryId eq categoryId }.map { it.response() }
    }
}