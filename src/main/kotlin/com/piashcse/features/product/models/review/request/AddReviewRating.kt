package com.piashcse.features.product.models.review.request

import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class AddReviewBody(
    val productId: Int,
    val reviewText: String?,
    val rating: Double
) {
    fun validation() {
        validate(this) {
            validate(AddReviewBody::productId).isNotNull()
            validate(AddReviewBody::rating).isNotNull()
        }
    }
}
