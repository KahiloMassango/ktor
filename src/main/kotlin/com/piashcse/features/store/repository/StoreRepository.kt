package com.piashcse.features.store.repository

import com.piashcse.features.store.models.request.StoreRegistrationBody
import com.piashcse.features.store.models.request.StoreUpdateBody
import com.piashcse.features.store.models.response.StoreResponse
import com.piashcse.features.store.models.response.StoreStatusResponse
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse

interface StoreRepository {
    suspend fun login(loginBody: LoginBody): LoginResponse<StoreResponse>
    suspend fun addStore(store: StoreRegistrationBody): StoreResponse
    suspend fun getAllStores(): List<StoreResponse>
    suspend fun getStoreById(storeId: Int): StoreResponse
    suspend fun updateStoreStatus(storeId: Int, isActive: Boolean): StoreStatusResponse
    suspend fun updateStore(storeId: Int, storeUpdateBody: StoreUpdateBody): StoreResponse
    suspend fun updateStoreLogo(storeId: Int, logoUrl: String): StoreResponse
}