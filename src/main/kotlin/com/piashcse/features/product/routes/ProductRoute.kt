package com.piashcse.features.product.routes

import com.piashcse.features.product.controllers.ProductController
import com.piashcse.features.product.models.product.response.ProductResponse
import com.piashcse.features.product.models.product.response.ProductWithVariationResponse
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.extension.apiResponse
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRoute(
    productController: ProductController,
) {
    route("products") {
        get("{category}/{sub-category}", {
            summary = "Return products within the given category and sub category"
            tags("Product")
            request {
                pathParameter<String>("category") {
                    required = true
                }
                pathParameter<String>("sub-category") {
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Successful"
                    body<List<ProductResponse>> {
                        description = "Successful"
                    }
                }
            }
        }) {
            val category = call.parameters["category"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val subCategory = call.parameters["sub-category"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                HttpStatusCode.OK,
                productController.getProducts(category, subCategory),
            )
        }

        get("{id}", {
            summary = "Return a product with its variations"
            tags("Product")
            request {
                pathParameter<Int>("id") {
                    required = true
                    description = "Id do produto"
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Successful"
                    body<ProductWithVariationResponse> {
                        description = "Successful"
                    }
                }
            }
        }) {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                HttpStatusCode.OK,
                productController.getProductById(id.toInt())
            )
        }

    }

}