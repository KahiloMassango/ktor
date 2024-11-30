package com.piashcse.features.product.controllers

import com.piashcse.features.customer.entities.CustomerEntity
import com.piashcse.features.product.entities.ProductEntity
import com.piashcse.features.product.entities.ProductTable
import com.piashcse.features.product.entities.ReviewEntity
import com.piashcse.features.product.entities.ReviewTable
import com.piashcse.features.product.models.review.request.AddReviewBody
import com.piashcse.features.product.models.review.response.ReviewResponse
import com.piashcse.features.product.repository.ReviewRatingRepository
import com.piashcse.features.store.entities.StoreTable
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll

class ReviewController : ReviewRatingRepository {

    override suspend fun getProductReviews(productId: Int): List<ReviewResponse> = query {
        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { ProductTable.id eq productId }
            .andWhere { ProductTable.isAvailable eq true }
            .andWhere { StoreTable.isActive eq true }

            .singleOrNull() ?: throw NotFoundException()

        val product = ProductEntity.wrapRow(query).response()

        ReviewEntity.find { ReviewTable.productId eq product.id }
            .orderBy(ReviewTable.createdAt to SortOrder.DESC)
            .map { it.response() }

    }

    override suspend fun addReview(customerId: Int, review: AddReviewBody): ReviewResponse = query {

        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { ProductTable.id eq review.productId }
            .andWhere { ProductTable.isAvailable eq true }
            .andWhere { StoreTable.isActive eq true }
            .singleOrNull() ?: throw review.productId.notFoundException()

        val product = ProductEntity.wrapRow(query)
        val customer = CustomerEntity.findById(customerId) ?: throw customerId.notFoundException()

        ReviewEntity.new {
            this.customerId = customer.id
            productId = product.id
            reviewText = review.reviewText
            rating = review.rating
        }.response()
    }

    override suspend fun updateReview(reviewId: Int, review: String, rating: Double): ReviewResponse = query {
        ReviewEntity.find { ReviewTable.productId eq reviewId }.toList().singleOrNull()
            ?.let {
                it.rating = rating
                it.reviewText = review
                it.response()
            } ?: throw reviewId.notFoundException()
    }

    override suspend fun deleteReview(reviewId: Int): Int = query {
        ReviewEntity.findById(reviewId)?.let {
            it.delete()
            reviewId
        } ?: throw reviewId.notFoundException()
    }

    override suspend fun getStoreProductReviews(productId: Int, storeId: Int): List<ReviewResponse> = query {
        val query = ReviewTable.innerJoin(StoreTable).innerJoin(ProductTable).selectAll()
            .andWhere { StoreTable.id eq storeId }
            .andWhere { StoreTable.isActive eq true }
            .andWhere { ProductTable.id eq productId }

        ReviewEntity.wrapRows(query).map { it.response() }
    }
}