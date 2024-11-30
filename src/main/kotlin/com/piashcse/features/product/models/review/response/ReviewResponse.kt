package com.piashcse.features.product.models.review.response

data class ReviewResponse(
    val id: Int,
    val customerName: String,
    val customerImage: String?,
    val reviewText: String?,
    val rating: Double,
)


