package com.piashcse.features.payment.entities

import com.piashcse.features.order.entities.OrderTable
import com.piashcse.features.payment.models.response.PaymentResponse
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object PaymentTable : BaseIntIdTable("payment") {
    val orderId = reference("order_id", OrderTable.id)
    val amount = double("amount")
    val status = varchar("status", 50) // e.g., "PENDING", "COMPLETED"
    val paymentMethod = varchar("payment_method", 50) // e.g., "CREDIT_CARD", "PAYPAL"
}

class PaymentEntity(id: EntityID<Int>) : BaseIntEntity(id, PaymentTable) {
    companion object : BaseIntEntityClass<PaymentEntity>(PaymentTable) // Reference the Payments table
    var paymentId by PaymentTable.id
    var orderId by PaymentTable.orderId
    var amount by PaymentTable.amount
    var status by PaymentTable.status
    var paymentMethod by PaymentTable.paymentMethod

    fun response() = PaymentResponse(
        id = paymentId.value,
        orderId = orderId.value,
        amount = amount,
        status = status,
        paymentMethod = paymentMethod,
    )
}


