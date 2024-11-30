package com.piashcse.features.product.models.product.response

import com.piashcse.features.product.models.review.response.ReviewResponse

data class ProductResponse(
    val id: Int,
    val title: String,
    val description: String,
    val store: ProductStoreResponse,
    val image: String,
    //val price: Double
)
