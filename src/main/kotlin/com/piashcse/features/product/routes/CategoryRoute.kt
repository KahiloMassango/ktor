package com.piashcse.features.product.routes

import com.piashcse.features.product.models.category.request.AddCategoryBody
import com.piashcse.features.product.models.category.request.CategoryUpdateBody
import com.piashcse.features.product.models.category.response.CategoryResponse
import com.piashcse.features.product.models.category.response.SubCategoryResponse
import com.piashcse.features.product.repository.CategoryRepository
import com.piashcse.features.product.repository.SubCategoryRepository
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.extension.apiResponse
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categoryRoute(
    categoryRepository: CategoryRepository,
    subCategoryRepository: SubCategoryRepository
) {
    route("category") {
        // Category
        get("", {
            summary = "Return all categories"
            tags("Category")
            apiResponse<List<CategoryResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    categoryRepository.getAllCategories(),
                    HttpStatusCode.OK
                )
            )
        }
        get("{categoryId}/sub-categories", {
            summary = "Return all sub categories of the given category"
            tags("Category")
            request {
                pathParameter<Int>("categoryId")
            }
            apiResponse<List<SubCategoryResponse>>()
        }) {
            val categoryId = call.parameters["categoryId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    categoryRepository.getCategorySubCategories(categoryId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

        post({
            summary = "Add new category"
            tags("Category")
            request {
                body<AddCategoryBody>()
            }
            apiResponse<CategoryResponse>()
        }) {
            val newCategory = call.receive<AddCategoryBody>()
            newCategory.validation()

            call.respond(
                ApiResponse.success(
                    categoryRepository.addCategory(newCategory),
                    HttpStatusCode.OK
                )
            )
        }

        put("{id}", {
            summary = "Update category"
            tags("Category")
            request {
                pathParameter<Int>("id")
                body<CategoryUpdateBody>()
            }
            apiResponse<CategoryResponse>()
        }) {

            val categoryUpdate = call.receive<CategoryUpdateBody>()
            categoryUpdate.validation()
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)

            call.respond(
                ApiResponse.success(
                    categoryRepository.updateCategory(id.toInt(), categoryUpdate),
                    HttpStatusCode.OK
                )
            )
        }


    }
}