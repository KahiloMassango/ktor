package com.piashcse.features.product.routes

import com.piashcse.features.product.controllers.VariationOptionController
import com.piashcse.features.product.models.variation.request.AddVariationOptionBody
import com.piashcse.features.product.models.variation.request.VariationOptionUpdateBody
import com.piashcse.features.product.models.variation.request.VariationUpdateBody
import com.piashcse.features.product.models.variation.response.VariationOptionResponse
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.extension.apiResponse
import io.github.smiley4.ktorswaggerui.dsl.routing.delete
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.variationOptionRoute(
    variationOptionController: VariationOptionController
) {
    route("variation-option") {
        get( {
            summary = "Return all variation options"
            tags("Variation Option")
            request {

            }
            apiResponse<List<VariationOptionResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    variationOptionController.getVariationOptions(),
                    HttpStatusCode.OK
                )
            )
        }

        post({
            summary = "Add new variation"
            tags("Variation Option")
            request {
                body<AddVariationOptionBody>()
            }
            apiResponse<VariationOptionResponse>()
        }
        ) {
            val newVariationOption = call.receive<AddVariationOptionBody>()
            newVariationOption.validation()

            call.respond(
                ApiResponse.success(
                    variationOptionController.addVariationOption(newVariationOption),
                    HttpStatusCode.OK
                )
            )
        }
        put("{id}", {
            summary = "Update variation"
            tags("Variation Option")
            request {
                pathParameter<Int>("id")
                body<VariationOptionUpdateBody>()
            }
            apiResponse<VariationOptionResponse>()
        }
        ) {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val updateVariation = call.receive<VariationOptionUpdateBody>()
            updateVariation.validation()

            call.respond(
                ApiResponse.success(
                    variationOptionController.updateVariationOption(id.toInt(), updateVariation),
                    HttpStatusCode.OK
                )
            )
        }

        delete("{id}", {
            summary = "Delete variation"
            tags("Variation Option")
            request {
                pathParameter<Int>("id")
            }
            apiResponse<Int>()
        }
        ) {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            call.respond(
                ApiResponse.success(
                    variationOptionController.deleteVariationOption(id.toInt()),
                    HttpStatusCode.OK
                )
            )
        }
    }
}