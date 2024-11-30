package com.piashcse.features.product.routes

import com.piashcse.features.product.models.category.request.AddSubCategoryBody
import com.piashcse.features.product.models.category.request.SubCategoryUpdateBody
import com.piashcse.features.product.models.category.response.SubCategoryResponse
import com.piashcse.features.product.models.variation.response.VariationResponse
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

fun Route.subCategoryRoute(
    subCategoryController: SubCategoryRepository
) {
    route("sub-category") {
        get({
            summary = "Return all sub categories"
            tags("Sub Category")
            apiResponse<List<SubCategoryResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    subCategoryController.getAllSubCategories(),
                    HttpStatusCode.OK
                )
            )
        }


        get("{subcategoryId}/variations", {
            summary = "Return variations of the given sub category"
            tags("Sub Category")
            request {
                pathParameter<Int>("subcategoryId")
            }
            apiResponse<List<VariationResponse>>()
        }) {
            val subcategoryId = call.parameters["subcategoryId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    subCategoryController.getSubCategoryVariations(subcategoryId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

        post({
            summary = "Add new sub category"
            tags("Sub Category")
            request {
                body<AddSubCategoryBody>()
            }
            apiResponse<List<VariationResponse>>()
        }) {
            val newSubCategory = call.receive<AddSubCategoryBody>()
            newSubCategory.validation()

            call.respond(
                ApiResponse.success(
                    subCategoryController.addSubCategory(newSubCategory),
                    HttpStatusCode.OK
                )
            )
        }

        put("{id}", {
            summary = "Update sub category"
            tags("Sub Category")
            request {
                body<SubCategoryUpdateBody>()
                pathParameter<Int>("id"){
                    required = true
                    description = "sub category id"
                }
            }
            apiResponse<SubCategoryResponse>()
        }) {

            val subCategoryUpdate = call.receive<SubCategoryUpdateBody>()
            subCategoryUpdate.validation()
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)

            call.respond(
                ApiResponse.success(
                    subCategoryController.updateSubCategory(id.toInt(), subCategoryUpdate),
                    HttpStatusCode.OK
                )
            )
        }
    }
}