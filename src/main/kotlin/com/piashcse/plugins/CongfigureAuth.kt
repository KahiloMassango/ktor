package com.piashcse.plugins

import com.piashcse.shared.autorization.JwtTokenBody
import com.piashcse.shared.controller.JwtController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuth() {
    install(Authentication) {
        /**
         * Setup the JWT authentication to be used in Routing.
         * If the token is valid, the corresponding User is fetched from the database.
         * The User can then be accessed in each ApplicationCall.
         */
        jwt (RoleManagement.CUSTOMER.role){
            provideJwtAuthConfig(this, RoleManagement.CUSTOMER)
        }
        jwt(RoleManagement.ADMIN.role) {
            provideJwtAuthConfig(this, RoleManagement.ADMIN)
        }
        jwt(RoleManagement.STORE.role) {
            provideJwtAuthConfig(this, RoleManagement.STORE)
        }
    }
}

fun provideJwtAuthConfig(jwtConfig: JWTAuthenticationProvider.Config, userRole: RoleManagement) {
    jwtConfig.verifier(JwtController.verifier)
    jwtConfig.realm = "piashcse"
    jwtConfig.validate {
        val userId = it.payload.getClaim("userId").asInt()
        val email = it.payload.getClaim("email").asString()
        val userType = it.payload.getClaim("userType").asString()
        if (userType == userRole.role) {
            JwtTokenBody(userId, email, userType)
        } else null
    }
}

enum class RoleManagement(val role: String) {
    ADMIN("admin"),
    STORE("store"),
    CUSTOMER("customer")
}