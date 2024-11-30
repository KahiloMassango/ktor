package com.piashcse.features.customer.entities

import com.piashcse.plugins.RoleManagement
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.shared.autorization.JwtTokenBody
import com.piashcse.shared.controller.JwtController
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object CustomerTable : BaseIntIdTable("costumer") {
    val username = varchar("name", 50)
    val email = varchar("email", 50)
    val password = varchar("password", 200)
    val profileImage = varchar("profile_image", 200).nullable().default(null)
    val gender = varchar("gender", 10)
    val userType = varchar("user_type", 100).default(RoleManagement.CUSTOMER.role)
    val phoneNumber = varchar("mobile_number", 50)
    val rememberToken = varchar("remember_token", 50).nullable()
}

class CustomerEntity(id: EntityID<Int>) : BaseIntEntity(id, CustomerTable) {
    companion object : BaseIntEntityClass<CustomerEntity>(CustomerTable)
    var username by CustomerTable.username
    var email by CustomerTable.email
    var password by CustomerTable.password
    var gender by CustomerTable.gender
    val userType by CustomerTable.userType
    var profileImage by CustomerTable.profileImage
    var phoneNumber by CustomerTable.phoneNumber
    var rememberToken by CustomerTable.rememberToken

    fun response() = CustomerResponse(
        id.value,
        username,
        email,
        profileImage,
        gender,
        phoneNumber,
        rememberToken,

    )

    fun loggedInWithToken() = LoginResponse(
        response(),
        JwtController.tokenProvider(JwtTokenBody(id.value, email, userType))
    )
}

data class CustomerResponse(
    val id: Int,
    val username: String,
    val email: String,
    val profileImage: String?,
    val gender: String,
    val phoneNumber: String,
    val rememberToken: String?,
)


