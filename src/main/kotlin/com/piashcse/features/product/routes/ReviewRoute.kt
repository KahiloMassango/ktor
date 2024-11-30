package com.piashcse.features.product.routes

import com.piashcse.features.product.controllers.ReviewController
import com.piashcse.features.product.models.review.request.AddReviewBody
import com.piashcse.features.product.models.review.response.ReviewResponse
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.extension.apiResponse
import com.piashcse.utils.extension.currentUser
import com.piashcse.utils.extension.requiredParameters
import io.github.smiley4.ktorswaggerui.dsl.routing.delete
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.reviewRatingRoute(reviewController: ReviewController) {
    route("review") {

        get("{productId}",{
            summary = "Return reviews for the given product"
            tags("Review")
            request {
                pathParameter<Int>("productId") {
                    required = true
                }
            }
            apiResponse<List<ReviewResponse>>()
        }) {
            val id = call.parameters["productId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    reviewController.getProductReviews(id.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

    post({
        summary = "Add new reviw to a product"
        tags("Review")
        request {
            body<AddReviewBody>()
        }
        apiResponse<ReviewResponse>()
    }) {
        val reviewRating = call.receive<AddReviewBody>()
        call.respond(
            ApiResponse.success(
                reviewController.addReview(1, reviewRating),
                HttpStatusCode.OK
            )
        )

    }
    delete("{id}", {
        summary = "Delete review"
        tags("Review")
        request {
            pathParameter<Int>("id") {
                required = true
            }
        }
        apiResponse<Int>()
    }) {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
        call.respond(
            ApiResponse.success(
                reviewController.deleteReview(id.toInt()), HttpStatusCode.OK
            )
        )

    }}
}