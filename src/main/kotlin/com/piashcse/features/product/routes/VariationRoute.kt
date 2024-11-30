package com.piashcse.features.product.routes

import com.piashcse.features.product.controllers.VariationController
import com.piashcse.features.product.models.variation.request.AddVariationBody
import com.piashcse.features.product.models.variation.request.VariationUpdateBody
import com.piashcse.features.product.models.variation.response.VariationOptionResponse
import com.piashcse.features.product.models.variation.response.VariationResponse
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

fun Route.variationRoute(
    variationController: VariationController
) {
    route("variation") {
        get( {
            summary = "Return all variations"
            tags("Variation")
            request {

            }
            apiResponse< List<VariationResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    variationController.getVariations(),
                    HttpStatusCode.OK
                )
            )
        }

        get("{id}/options", {
            summary = "Return all options for the given variation"
            tags("Variation")
            request {
                pathParameter<Int>("id")
            }
            apiResponse<List<VariationOptionResponse>>()
        }) {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    variationController.getVariationOptions(id.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

        post({
            summary = "Add new variation"
            tags("Variation")
            request {
                body<AddVariationBody>()
            }
           apiResponse<VariationResponse>()
        }
        ) {
            val newVariation = call.receive<AddVariationBody>()
            newVariation.validation()

            call.respond(
                ApiResponse.success(
                    variationController.addVariation(newVariation),
                    HttpStatusCode.OK
                )
            )
        }
        put("{id}", {
            summary = "Update variation"
            tags("Variation")
            request {
                pathParameter<Int>("id")
                body<VariationUpdateBody>()
            }
           apiResponse<VariationResponse>()
        }
        ) {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val updateVariation = call.receive<VariationUpdateBody>()
            updateVariation.validation()

            call.respond(
                ApiResponse.success(
                    variationController.updateVariation(id.toInt(), updateVariation),
                    HttpStatusCode.OK
                )
            )
        }

        delete("{id}", {
            summary = "Delete variation"
            tags("Variation")
            request {
                pathParameter<Int>("id")
            }
           apiResponse<Int>()
        }
        ) {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            call.respond(
                ApiResponse.success(
                    variationController.deleteVariation(id.toInt()),
                    HttpStatusCode.OK
                )
            )
        }
    }
}