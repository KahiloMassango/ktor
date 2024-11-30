package com.piashcse.features.product.entities

import com.piashcse.features.customer.entities.CustomerEntity
import com.piashcse.features.customer.entities.CustomerTable
import com.piashcse.features.product.models.review.response.ReviewResponse
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object ReviewTable : BaseIntIdTable("review_rating") {
    val customerId = reference("customer_id", CustomerTable.id)
    val productId = reference("product_id", ProductTable.id)
    val reviewText = varchar("review_text", 300).nullable()
    val rating = double("rating").check { it.between(0.0, 5.0) }
}

class ReviewEntity(id: EntityID<Int>) : BaseIntEntity(id, ReviewTable) {
    companion object : BaseIntEntityClass<ReviewEntity>(ReviewTable)

    var customerId by ReviewTable.customerId
    var productId by ReviewTable.productId
    var reviewText by ReviewTable.reviewText
    var rating by ReviewTable.rating

    private val customer by CustomerEntity referencedOn ReviewTable.customerId

    fun response() = ReviewResponse(
        id = id.value,
        customerName = customer.username,
        customerImage = customer.profileImage,
        reviewText = reviewText,
        rating = rating,
    )
}

