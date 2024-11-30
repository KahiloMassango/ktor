package com.piashcse.features.store.models.response

data class StoreResponse(
    val id: Int,
    val storeName: String,
    val description: String,
    val nif: String,
    val email: String,
    val logo: String, 
    val banner: String?,
    val status: Boolean,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
    val rememberToken: String?
)