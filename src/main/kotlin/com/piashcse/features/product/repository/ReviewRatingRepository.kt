package com.piashcse.features.product.repository

import com.piashcse.features.product.models.review.request.AddReviewBody
import com.piashcse.features.product.models.review.response.ReviewResponse

interface ReviewRatingRepository {
    suspend fun getProductReviews(productId: Int): List<ReviewResponse>
    suspend fun getStoreProductReviews(productId: Int, storeId: Int): List<ReviewResponse>
    suspend fun addReview(customerId: Int, review: AddReviewBody): ReviewResponse
    suspend fun updateReview(reviewId: Int, review: String, rating: Double): ReviewResponse
    suspend fun deleteReview(reviewId: Int): Int
}