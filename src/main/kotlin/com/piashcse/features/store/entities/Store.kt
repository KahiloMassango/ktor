package com.piashcse.features.store.entities

import com.piashcse.features.store.models.response.StoreLoginResponse
import com.piashcse.features.store.models.response.StoreResponse
import com.piashcse.plugins.RoleManagement
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.shared.autorization.JwtTokenBody
import com.piashcse.shared.controller.JwtController
import com.piashcse.shared.entities.BaseIntEntity
import com.piashcse.shared.entities.BaseIntEntityClass
import com.piashcse.shared.entities.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object StoreTable : BaseIntIdTable("store") {
    val name = varchar("store_name", 50)
    val description = varchar("description", 255)
    val nif = varchar("nif", 100)
    val isActive = bool("is_active").default(false)
    val email = varchar("email", 50)
    val password = varchar("password", 200)
    val logo = varchar("logo_image", 200)
    val banner = varchar("banner_image", 200).nullable().default(null)
    val userType = varchar("user_type", 100).default(RoleManagement.STORE.role)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val phoneNumber = varchar("mobile_number", 50)
    val rememberToken = varchar("remember_token", 50).nullable()
    val verificationCode = varchar("verification_code", 30).nullable() // verification_code
}

class StoreEntity(id: EntityID<Int>) : BaseIntEntity(id, StoreTable) {
    companion object : BaseIntEntityClass<StoreEntity>(StoreTable)
    var name by StoreTable.name
    var email by StoreTable.email
    var password by StoreTable.password
    var description by StoreTable.description
    val userType by StoreTable.userType
    var logo by StoreTable.logo
    var banner by StoreTable.banner
    var isActive by StoreTable.isActive
    var latitude by StoreTable.latitude
    var longitude by StoreTable.longitude
    var nif by StoreTable.nif
    var phoneNumber by StoreTable.phoneNumber
    var rememberToken by StoreTable.rememberToken
    var verificationCode by StoreTable.verificationCode

    fun response() = StoreResponse(
        id.value,
        name,
        description,
        nif,
        email,
        logo,
        banner,
        isActive,
        latitude,
        longitude,
        phoneNumber,
        rememberToken,
    )

    fun loggedInWithToken() = LoginResponse(
        response(),
        JwtController.tokenProvider(JwtTokenBody(id.value, email, userType))
    )
}
