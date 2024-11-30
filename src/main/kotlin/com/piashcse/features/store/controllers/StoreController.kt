package com.piashcse.features.store.controllers

import at.favre.lib.crypto.bcrypt.BCrypt
import com.piashcse.features.store.entities.StoreEntity
import com.piashcse.features.store.entities.StoreTable
import com.piashcse.features.store.models.request.StoreRegistrationBody
import com.piashcse.features.store.models.request.StoreUpdateBody
import com.piashcse.features.store.models.response.StoreLoginResponse
import com.piashcse.features.store.models.response.StoreResponse
import com.piashcse.features.store.models.response.StoreStatusResponse
import com.piashcse.features.store.repository.StoreRepository
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.utils.AppConstants
import com.piashcse.utils.PasswordDoesNotMatch
import com.piashcse.utils.UserNotExistException
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query
import org.jetbrains.exposed.sql.and
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.mail.Store

class StoreController : StoreRepository {

    override suspend fun login(loginBody: LoginBody): LoginResponse<StoreResponse> = query {
        StoreEntity.find { StoreTable.email eq loginBody.email }
            .toList()
            .singleOrNull()?.let {
                if (
                    BCrypt.verifyer().verify(
                        loginBody.password.toCharArray(),
                        it.password
                    ).verified
                ) {
                    it.loggedInWithToken()
                } else throw PasswordDoesNotMatch()

            } ?: throw UserNotExistException()
    }

    override suspend fun addStore(store: StoreRegistrationBody): StoreResponse = query {
        StoreEntity.new {
            name = store.storeName
            description = store.description
            nif = store.nif
            email = store.email
            logo = store.logo
            banner = store.banner
            latitude = store.latitude
            longitude = store.longitude
            phoneNumber = store.phoneNumber
            password = BCrypt.withDefaults().hashToString(12, store.password.toCharArray())

        }.response()
    }

    override suspend fun getAllStores(): List<StoreResponse> = query {
        StoreEntity.all().map { it.response() }
    }

    override suspend fun getStoreById(storeId: Int): StoreResponse = query {
        StoreEntity.find { StoreTable.id eq storeId }.toList().singleOrNull()
            ?.response() ?: throw storeId.notFoundException()
    }

    override suspend fun updateStoreStatus(storeId: Int, isActive: Boolean): StoreStatusResponse = query {
        StoreEntity.find { StoreTable.id eq storeId }.toList().singleOrNull()
            ?.let {
                it.isActive = isActive
                StoreStatusResponse(it.id.value, it.name, it.isActive)
            } ?: throw storeId.notFoundException()


    }


    override suspend fun updateStore(storeId: Int, storeUpdateBody: StoreUpdateBody): StoreResponse = query {
        StoreEntity.findById(storeId)
            ?.let {
                it.name = storeUpdateBody.storeName
                it.description = storeUpdateBody.description
                it.email = storeUpdateBody.email
                it.latitude = storeUpdateBody.latitude
                it.phoneNumber = storeUpdateBody.phoneNumber
                it.response()
            } ?: throw storeId.notFoundException()
    }

    override suspend fun updateStoreLogo(storeId: Int, logoUrl: String): StoreResponse = query {
        val store = StoreEntity.find { (StoreTable.id eq storeId) and (StoreTable.isActive eq true) }.singleOrNull()
        store?.logo?.let {
            val imageName = it.substring(it.lastIndexOf("/") + 1)
            Files.deleteIfExists(Paths.get(
                "${AppConstants.Image.RESOURCES_PATH}${AppConstants.Image.STORE_PATH}$imageName"))
        }

        store?.let {
            it.logo = logoUrl
            it.response()
        } ?: throw storeId.notFoundException()
    }
}