package com.piashcse.features.product.repository

import com.piashcse.features.product.models.product.request.AddProductBody
import com.piashcse.features.product.models.product.request.ProductSearch
import com.piashcse.features.product.models.product.request.UpdateProductBody
import com.piashcse.features.product.models.product.response.ProductResponse
import com.piashcse.features.product.models.product.response.ProductWithVariationResponse

interface ProductRepository {
    suspend fun addProduct(storeId: Int, productBody: AddProductBody): ProductWithVariationResponse
    suspend fun updateProduct(storeId: Int, productId: Int, updateProductBody: UpdateProductBody): ProductWithVariationResponse

    suspend fun getProductById(productId: Int): ProductWithVariationResponse

    suspend fun getProductByStoreId(storeId: Int, productId: Int): ProductWithVariationResponse
    suspend fun getProductsByStoreId(storeId: Int): List<ProductResponse>
    suspend fun getStoreProducts(storeId: Int): List<ProductResponse>

    suspend fun deleteProduct(storeId: Int, productId: Int): Int

    suspend fun uploadProductImage(storeId: Int, productId: Int, imageUrl: String): ProductWithVariationResponse

    suspend fun getProducts(category: String, subCategory: String): List<ProductResponse>

    suspend fun getProductsByFilter(productQuery: ProductSearch): List<ProductResponse>

}