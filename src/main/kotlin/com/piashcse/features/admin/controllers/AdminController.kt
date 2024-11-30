package com.piashcse.features.admin.controllers

import at.favre.lib.crypto.bcrypt.BCrypt
import com.piashcse.features.admin.entities.AdminEntity
import com.piashcse.features.admin.entities.AdminTable
import com.piashcse.features.admin.models.request.AdminRegistrationBody
import com.piashcse.features.admin.models.request.AdminUpdateBody
import com.piashcse.features.admin.models.response.AdminResponse
import com.piashcse.features.admin.repository.AdminRepository
import com.piashcse.features.customer.entities.CustomerEntity
import com.piashcse.features.customer.entities.CustomerResponse
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.utils.PasswordDoesNotMatch
import com.piashcse.utils.UserNotExistException
import com.piashcse.utils.extension.alreadyExistException
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query

class AdminController : AdminRepository {

    override suspend fun getAdminByEmail(email: String): AdminResponse = query {
        val adminExists = AdminEntity.find { AdminTable.email eq email }.toList().singleOrNull()
        adminExists?.response() ?: throw email.notFoundException()
    }


    override suspend fun updateAdminProfile(adminId: Int, adminUpdate: AdminUpdateBody): AdminResponse = query {
        AdminEntity.find { AdminTable.id eq adminId }
            .toList()
            .singleOrNull()?.let {
            it.username = adminUpdate.username
            it.email = adminUpdate.email
            it.password = BCrypt.withDefaults().hashToString(12, adminUpdate.password.toCharArray())
            it.response()
        } ?: throw adminId.notFoundException()
    }

    override suspend fun addAdmin(registrationBody: AdminRegistrationBody): AdminResponse = query {
        AdminEntity.find { AdminTable.email eq registrationBody.email }
            .toList().singleOrNull()?.let{ throw registrationBody.email.alreadyExistException("as Admin") }

        AdminEntity.new {
            username = registrationBody.username
            email = registrationBody.email
            phoneNumber = registrationBody.phoneNumber
            password = BCrypt.withDefaults().hashToString(12, registrationBody.password.toCharArray())

        }.response()
    }

    override suspend fun getAllCustomers(): List<CustomerResponse> = query {
        CustomerEntity.all().map { it.response() }
    }

    override suspend fun login(loginBody: LoginBody): LoginResponse<AdminResponse> = query {
        AdminEntity.find { AdminTable.email eq loginBody.email }
            .toList()
            .singleOrNull()?.let {
            if (BCrypt.verifyer().verify(
                    loginBody.password.toCharArray(),
                    it.password
                ).verified
            ) {
                it.loggedInWithToken()
            } else {
                throw PasswordDoesNotMatch()
            }

        } ?: throw UserNotExistException()
    }
}
