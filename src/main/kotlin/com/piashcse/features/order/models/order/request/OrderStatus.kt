package com.piashcse.features.order.models.order.request

enum class OrderStatus(val status: String) {
    PROCESSING("processing"),
    CONFIRMED("confirmed"),
    DELIVERED("delivered"),
    CANCELED("canceled")
}

