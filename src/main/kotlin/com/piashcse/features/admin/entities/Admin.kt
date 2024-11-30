package com.piashcse.features.admin.entities

import com.piashcse.features.admin.models.response.AdminResponse
import com.piashcse.plugins.RoleManagement
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.shared.autorization.JwtTokenBody
import com.piashcse.shared.controller.JwtController
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object AdminTable : BaseIntIdTable("admin") {
    val username = varchar("name", 50)
    val email = varchar("email", 50)
    val password = varchar("password", 200)
    val userType = varchar("user_type", 100).default(RoleManagement.ADMIN.role)
    val phoneNumber = varchar("mobile_number", 50)
    val rememberToken = varchar("remember_token", 50).nullable()
}

class AdminEntity(id: EntityID<Int>) : BaseIntEntity(id, AdminTable) {
    companion object : BaseIntEntityClass<AdminEntity>(AdminTable)
    var username by AdminTable.username
    var email by AdminTable.email
    var password by AdminTable.password
    val userType by AdminTable.userType
    var phoneNumber by AdminTable.phoneNumber
    var rememberToken by AdminTable.rememberToken

    fun response() = AdminResponse(
        id.value,
        username,
        email,
    )

    fun loggedInWithToken() = LoginResponse(
        response(),
        JwtController.tokenProvider(JwtTokenBody(id.value, email, userType))
    )
}

