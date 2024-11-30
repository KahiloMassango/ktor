package com.piashcse.features.customer.controllers

import at.favre.lib.crypto.bcrypt.BCrypt
import com.piashcse.features.customer.entities.CustomerResponse
import com.piashcse.features.customer.entities.CustomerEntity
import com.piashcse.features.customer.entities.CustomerTable
import com.piashcse.features.customer.models.request.CostumerRegistrationBody
import com.piashcse.features.customer.models.request.CustomerUpdateBody
import com.piashcse.features.customer.models.response.CostumerRegistrationResponse
import com.piashcse.features.customer.repository.CostumerRepository
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.utils.AppConstants
import com.piashcse.utils.UserNotExistException
import com.piashcse.utils.extension.alreadyExistException
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query
import java.nio.file.Files
import java.nio.file.Paths

class CustomerController : CostumerRepository {
    override suspend fun login(loginBody: LoginBody): LoginResponse<CustomerResponse> = query {
        CustomerEntity.find { CustomerTable.email eq loginBody.email }.toList().singleOrNull()
            ?.let {
                if (BCrypt.verifyer().verify(loginBody.password.toCharArray(), it.password).verified) {
                    it.loggedInWithToken()
                } else throw UserNotExistException()
            } ?: throw UserNotExistException()
    }

    override suspend fun addCustomer(registrationBody: CostumerRegistrationBody): CostumerRegistrationResponse = query {
        CustomerEntity.find { CustomerTable.email eq registrationBody.email }
            .toList()
            .singleOrNull() ?: throw registrationBody.email.alreadyExistException("email já existe")

        val inserted = CustomerEntity.new {
            username = registrationBody.username
            gender = registrationBody.gender
            phoneNumber = registrationBody.phoneNumber
            profileImage = registrationBody.profileImage
            email = registrationBody.email
            password = BCrypt.withDefaults().hashToString(12, registrationBody.password.toCharArray())
        }
        CostumerRegistrationResponse(inserted.id.value, inserted.username, inserted.email)
    }

    override suspend fun deleteCustomerById(customerId: Int): Int = query {
        val costumerExists = CustomerEntity.find { CustomerTable.id eq customerId }.toList().singleOrNull()
        costumerExists?.let {
            it.delete()
            customerId
        } ?: throw customerId.notFoundException()
    }

    override suspend fun getAllCostumers(): List<CustomerResponse> = query {
        CustomerEntity.all().map { it.response() }
    }

    override suspend fun getCustomerById(customerId: Int): CustomerResponse = query {
        CustomerEntity.find { CustomerTable.id eq customerId }
            .toList()
            .singleOrNull()?.response() ?: throw customerId.notFoundException()
    }

    override suspend fun updateCustomer(customerId: Int, costumerUpdate: CustomerUpdateBody): CustomerResponse = query {
        CustomerEntity.findById(customerId)?.let {
            it.username = costumerUpdate.username
            it.email = costumerUpdate.email
            it.phoneNumber = costumerUpdate.phoneNumber
            it.response()
        } ?: throw customerId.notFoundException()
    }

    override suspend fun updateCustomerProfileImage(customerId: Int, imageUrl: String): CustomerResponse = query {
        val customer = CustomerEntity.findById(customerId) ?: throw customerId.notFoundException()
        customer.profileImage?.let {
            val imageName = it.substring(it.lastIndexOf("/") + 1)
            Files.deleteIfExists(Paths.get(
                "${AppConstants.Image.RESOURCES_PATH}${AppConstants.Image.CUSTOMER_PATH}$imageName"))
        }
        customer.let {
            it.profileImage = imageUrl
            it.response()
        }
    }
}