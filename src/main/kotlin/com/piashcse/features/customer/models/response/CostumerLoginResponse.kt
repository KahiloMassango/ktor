package com.piashcse.features.customer.models.response

import com.piashcse.features.customer.entities.CustomerResponse

data class CostumerLoginResponse(
    val costumer: CustomerResponse, val accessToken: String
)
