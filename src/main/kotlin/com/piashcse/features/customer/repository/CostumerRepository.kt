package com.piashcse.features.customer.repository

import com.piashcse.features.customer.entities.CustomerResponse
import com.piashcse.features.customer.models.request.CostumerRegistrationBody
import com.piashcse.features.customer.models.request.CustomerUpdateBody
import com.piashcse.features.customer.models.response.CostumerRegistrationResponse
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse

interface CostumerRepository {
    suspend fun login(loginBody: LoginBody): LoginResponse<CustomerResponse>
    suspend fun addCustomer(registrationBody: CostumerRegistrationBody): CostumerRegistrationResponse
    suspend fun deleteCustomerById(customerId: Int): Int
    suspend fun getAllCostumers(): List<CustomerResponse>
    suspend fun getCustomerById(customerId: Int): CustomerResponse
    suspend fun updateCustomerProfileImage(customerId: Int, imageUrl: String): CustomerResponse
    suspend fun updateCustomer(customerId: Int, costumerUpdate: CustomerUpdateBody): CustomerResponse
}