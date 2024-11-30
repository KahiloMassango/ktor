package com.piashcse.features.admin.repository

import com.piashcse.features.admin.models.request.AdminRegistrationBody
import com.piashcse.features.admin.models.request.AdminUpdateBody
import com.piashcse.features.admin.models.response.AdminResponse
import com.piashcse.features.customer.entities.CustomerResponse
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse


interface AdminRepository {
    suspend fun getAdminByEmail(email: String): AdminResponse
    suspend fun updateAdminProfile(adminId: Int, adminUpdate: AdminUpdateBody): AdminResponse
    suspend fun addAdmin(registrationBody: AdminRegistrationBody): AdminResponse
    suspend fun login(loginBody: LoginBody): LoginResponse<AdminResponse>
    suspend fun getAllCustomers(): List<CustomerResponse>

}