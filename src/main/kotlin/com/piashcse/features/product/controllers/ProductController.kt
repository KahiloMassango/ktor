package com.piashcse.features.product.controllers

import com.piashcse.features.product.entities.*
import com.piashcse.features.product.models.product.request.AddProductBody
import com.piashcse.features.product.models.product.request.ProductSearch
import com.piashcse.features.product.models.product.request.UpdateProductBody
import com.piashcse.features.product.models.product.response.ProductResponse
import com.piashcse.features.product.models.product.response.ProductWithVariationResponse
import com.piashcse.features.product.repository.ProductRepository
import com.piashcse.features.store.entities.StoreEntity
import com.piashcse.features.store.entities.StoreTable
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ProductController : ProductRepository {

    override suspend fun addProduct(storeId: Int, productBody: AddProductBody): ProductWithVariationResponse = query {
        // Pre-fetch entities outside the transaction
        val store = StoreEntity.findById(storeId) ?: throw storeId.notFoundException()
        val category =
            CategoryEntity.findById(productBody.categoryId) ?: throw productBody.categoryId.notFoundException()
        val subCategory =
            SubCategoryEntity.findById(productBody.subCategoryId) ?: throw productBody.subCategoryId.notFoundException()


        // Pre-fetch size and color options to avoid suspending calls in the transaction
        val sizeMap = productBody.variations.mapNotNull { variation ->
            variation.sizeId?.let { sizeId -> sizeId to VariationOptionEntity.findById(sizeId) }
        }.toMap()

        val colorMap = productBody.variations.mapNotNull { variation ->
            variation.colorId?.let { colorId -> colorId to VariationOptionEntity.findById(colorId) }
        }.toMap()

        // Perform transaction
        transaction {
            val product = ProductEntity.new {
                this.storeId = store.id
                title = productBody.title
                description = productBody.description
                categoryId = category.id
                subCategoryId = subCategory.id
                isAvailable = productBody.isAvailable
                image = productBody.image
            }

            productBody.variations.forEach { variation ->
                if (variation.sizeId != null || variation.colorId != null) {
                    val productItem = ProductItemEntity.new {
                        productId = product.id
                        qtyStock = variation.qtyStock
                        image = variation.image
                        price = variation.price
                    }

                    // Create configurations for size
                    sizeMap[variation.sizeId]?.let { sizeEntity ->
                        ProductConfigurationEntity.new {
                            productItemId = productItem.id
                            variationOptionId = sizeEntity.id
                        }
                    }

                    // Create configurations for color
                    colorMap[variation.colorId]?.let { colorEntity ->
                        ProductConfigurationEntity.new {
                            productItemId = productItem.id
                            variationOptionId = colorEntity.id
                        }
                    }
                }
            }
            product.responseWithVariation()
        }
    }

    override suspend fun updateProduct(
        storeId: Int,
        productId: Int,
        updateProductBody: UpdateProductBody
    ): ProductWithVariationResponse = query {

        val category = CategoryEntity.find { CategoryTable.id eq updateProductBody.categoryId }.toList().singleOrNull()
            ?: throw updateProductBody.categoryId.notFoundException()

        val subCategory =
            SubCategoryEntity.find { SubCategoryTable.id eq updateProductBody.subCategoryId }.toList().singleOrNull()
                ?: throw updateProductBody.subCategoryId.notFoundException()

        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { ProductTable.storeId eq storeId }
            .andWhere { StoreTable.isActive eq true }
            .andWhere { ProductTable.id eq productId }
            .singleOrNull() ?: throw productId.notFoundException()

        ProductEntity.wrapRow(query).let {
            it.title = updateProductBody.productName
            it.description = updateProductBody.description
            it.categoryId = category.id
            it.subCategoryId = subCategory.id
            it.image = updateProductBody.image

            it.responseWithVariation()
        }

    }

    override suspend fun getProductById(productId: Int): ProductWithVariationResponse = query {
        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { ProductTable.id eq productId }
            .andWhere { ProductTable.isAvailable eq true }
            .andWhere { StoreTable.isActive eq true }
            .limit(1)
            .singleOrNull() ?: throw productId.notFoundException()

        ProductEntity.wrapRow(query).responseWithVariation()
    }

    override suspend fun deleteProduct(storeId: Int, productId: Int): Int = query {
        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { ProductTable.id eq productId }
            .andWhere { StoreTable.id eq storeId }
            .andWhere { StoreTable.isActive eq true }
            .limit(1)
            .singleOrNull() ?: throw productId.notFoundException()

        ProductEntity.wrapRow(query).let {
            it.delete()
            productId
        }
    }

    override suspend fun getProducts(category: String, subCategory: String): List<ProductResponse> = query {
        val categoryEntity = CategoryEntity.find { CategoryTable.name eq category }.singleOrNull()
            ?: throw category.notFoundException()

        val subCategoryEntity = SubCategoryEntity.find {
            (SubCategoryTable.name eq subCategory) and (SubCategoryTable.categoryId eq categoryEntity.id)
        }.singleOrNull() ?: throw subCategory.notFoundException()

        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { StoreTable.isActive eq true }
            .andWhere { ProductTable.categoryId eq categoryEntity.id }
            .andWhere { ProductTable.subCategoryId eq subCategoryEntity.id }
            .andWhere { ProductTable.isAvailable eq true }
            .limit(50)

        ProductEntity.wrapRows(query).map { it.response() }

    }

    override suspend fun uploadProductImage(
        storeId: Int,
        productId: Int,
        imageUrl: String
    ): ProductWithVariationResponse = query {

        ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { ProductTable.id eq productId }
            .andWhere { ProductTable.storeId eq storeId }
            .andWhere { StoreTable.isActive eq true }
            .limit(1)
            .singleOrNull()?.let { resultRow ->
                ProductEntity.wrapRow(resultRow).let {
                    it.image = imageUrl
                    it.responseWithVariation()
                }
            } ?: throw productId.notFoundException()

    }

    override suspend fun getStoreProducts(storeId: Int): List<ProductResponse> = query {
        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { ProductTable.storeId eq storeId }
            .andWhere { StoreTable.isActive eq true }
            .andWhere { ProductTable.isAvailable eq true }
            .limit(50)

        ProductEntity.wrapRows(query).map { it.response() }
    }

    override suspend fun getProductsByStoreId(storeId: Int): List<ProductResponse> = query {
        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { StoreTable.id eq storeId }
            .andWhere { StoreTable.isActive eq true }


        ProductEntity.wrapRows(query).map { it.response() }
    }

    override suspend fun getProductByStoreId(storeId: Int, productId: Int): ProductWithVariationResponse = query {
        val query = ProductTable.innerJoin(StoreTable).selectAll()
            .andWhere { StoreTable.id eq storeId }
            .andWhere { StoreTable.isActive eq true }
            .andWhere { ProductTable.id eq productId }
            .limit(1)

        ProductEntity.wrapRows(query).singleOrNull()?.responseWithVariation() ?: throw productId.notFoundException()

    }

    override suspend fun getProductsByFilter(productQuery: ProductSearch): List<ProductResponse> = query {
        val query = ProductTable.selectAll()

        productQuery.productName.let {
            query.andWhere { ProductTable.title like "%$it%" }
        }


        productQuery.productName.let {
            query.andWhere { ProductTable.title like "%$it%" }
        }

        query.limit(productQuery.limit ?: 20)
        query.orderBy(ProductTable.createdAt, SortOrder.DESC).map {
            ProductEntity.wrapRow(it).response()
        }

    }
}