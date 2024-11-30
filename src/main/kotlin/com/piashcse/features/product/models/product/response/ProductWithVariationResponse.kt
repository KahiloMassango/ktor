package com.piashcse.features.product.models.product.response

import com.piashcse.features.product.entities.ProductItemResponse
import com.piashcse.features.product.models.review.response.ReviewResponse


data class ProductWithVariationResponse(
    val productId: Int,
    val title: String,
    val description: String,
    val store: ProductStoreResponse,
    val image: String,
    val subCategory: String,
    val productItems: List<ProductItemResponse>,
    val reviews: List<ReviewResponse>
)

data class ProductStoreResponse(
    val id: Int,
    val name: String
)



