package com.piashcse.features.store.models.response

data class StoreLoginResponse(
    val store: StoreResponse, val accessToken: String
)
