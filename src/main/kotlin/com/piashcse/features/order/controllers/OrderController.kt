package com.piashcse.features.order.controllers

import com.piashcse.features.customer.entities.CustomerEntity
import com.piashcse.features.order.entities.*
import com.piashcse.features.order.models.order.request.AddOrderBody
import com.piashcse.features.order.models.order.request.DeliveryMethod
import com.piashcse.features.order.models.order.request.OrderStatus
import com.piashcse.features.order.models.order.request.OrderStatusUpdateBody
import com.piashcse.features.order.models.order.response.CustomerOrderItemResponse
import com.piashcse.features.order.models.order.response.CustomerOrderResponse
import com.piashcse.features.order.models.order.response.OrderResponse
import com.piashcse.features.order.repository.OrderRepository
import com.piashcse.features.product.entities.ProductEntity
import com.piashcse.features.product.entities.ProductItemEntity
import com.piashcse.features.product.entities.ProductItemTable
import com.piashcse.features.product.entities.ProductTable
import com.piashcse.features.store.entities.StoreEntity
import com.piashcse.features.store.entities.StoreTable
import com.piashcse.features.store.models.response.StoreOrderResponse
import com.piashcse.utils.CommonException
import com.piashcse.utils.extension.notFoundException
import com.piashcse.utils.extension.query
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.orWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class OrderController : OrderRepository {

    override suspend fun addOrder(customerId: Int, orderBody: AddOrderBody): OrderResponse = query {
        // Validate the customer
        val customer = CustomerEntity.findById(customerId) ?: throw customerId.notFoundException("Customer not found")

        // Validate that all product items belong to the same store
        val productItemIds = orderBody.orderItems.map { it.productItemId }

        val storeIds = ProductItemTable.innerJoin(ProductTable)
            .select(ProductTable.storeId)
            .where { ProductItemTable.id inList productItemIds }
            .map { it[ProductTable.storeId].value }
            .distinct()

        if (storeIds.size != 1) {
            throw CommonException("All product items must belong to the same store")
        }

        // Validate the store
        val store = StoreEntity.find { (StoreTable.id eq storeIds[0]) and (StoreTable.isActive eq true) }.singleOrNull()
            ?: throw storeIds[0].notFoundException("Store not found")

        // Map product item IDs to their corresponding entities
        val productItemMap = orderBody.orderItems.associate { orderItem ->
            orderItem.productItemId to (ProductItemEntity.findById(orderItem.productItemId)
                ?: throw orderItem.productItemId.notFoundException("Product Item not found"))
        }

        // Fetch related products for product items
        val productMap = orderBody.orderItems.associate { orderItem ->
            orderItem.productItemId to (ProductEntity.find {
                (ProductTable.id eq productItemMap[orderItem.productItemId]!!.productId) and
                        (ProductTable.isAvailable eq true)
            }.singleOrNull()
                ?: throw orderItem.productItemId.notFoundException("Product orderItem not found"))
        }


        // Validate stock availability
        orderBody.orderItems.forEach { orderItem ->
            val productItem = productItemMap[orderItem.productItemId]!!
            if (productItem.qtyStock < orderItem.quantity) {
                throw CommonException("Insufficient stock for product: ${productMap[orderItem.productItemId]!!.title}")
            }
        }

        // Create the order and associated items
        transaction {
            val order = OrderEntity.new {
                this.customerId = customer.id
                this.storeId = store.id
                this.quantity = orderBody.orderItems.size
                this.subTotal = orderBody.subTotal
                this.total = orderBody.total
                this.deliveryMethod = orderBody.deliveryMethod
                this.deliveryFee = orderBody.deliveryFee
                this.deliveryLatitude = orderBody.deliveryLatitude
                this.deliveryLongitude = orderBody.deliveryLongitude
            }

            // Create order items and update stock
            orderBody.orderItems.forEach { orderItem ->
                val product = productMap[orderItem.productItemId]!!
                val productItem = productItemMap[orderItem.productItemId]!!

                // Deduct stock
                productItem.qtyStock -= orderItem.quantity

                // Create order item
                OrderItemEntity.new {
                    this.orderId = order.id
                    this.productName = product.title
                    this.quantity = orderItem.quantity
                    this.color = productItem.response().color
                    this.size = productItem.response().size
                    this.price = productItem.price
                }
            }

            if (orderBody.deliveryMethod == DeliveryMethod.DELIVERY.method) {
                DeliveryEntity.new {
                    orderId = order.id
                }
            }

            // Return order response
            order.response()
        }
    }


    override suspend fun getOrders(): List<OrderResponse> = query {
        OrderEntity.all().map { it.response() }
    }

    override suspend fun getOrderByStoreId(storeId: Int, orderId: Int): StoreOrderResponse = query {
        val query = OrderTable.selectAll()
            .andWhere { OrderTable.storeId eq storeId }
            .andWhere { OrderTable.id eq orderId }
            .orWhere { OrderTable.status eq OrderStatus.CONFIRMED.name.lowercase() }
            .orWhere { OrderTable.status eq OrderStatus.DELIVERED.name.lowercase() }
            .singleOrNull() ?: throw CommonException("loja ou encomenda não existe")

        val order = OrderEntity.wrapRow(query).response()
        val customer = CustomerEntity.findById(order.customerId)!!

        StoreOrderResponse(
            order.orderId,
            customer.username,
            customer.phoneNumber,
            order.subTotal,
            order.deliveryMethod,
            order.products
        )
    }

    override suspend fun getOrdersByStoreId(storeId: Int): List<StoreOrderResponse> = query {
        val query = OrderTable.selectAll()
            .andWhere { OrderTable.storeId eq storeId }
            .orWhere { OrderTable.status eq OrderStatus.CONFIRMED.name.lowercase() }
            .orWhere { OrderTable.status eq OrderStatus.DELIVERED.name.lowercase() }

        OrderEntity.wrapRows(query).map { order ->
            val customer = CustomerEntity.findById(order.customerId.value)!!

            StoreOrderResponse(
                order.id.value,
                customer.username,
                customer.phoneNumber,
                order.subTotal,
                order.deliveryMethod,
                order.response().products
            )
        }
    }

    override suspend fun getOrderByCustomerId(customerId: Int, orderId: Int): CustomerOrderResponse = query {


        val query = OrderTable.selectAll()
            .andWhere { OrderTable.customerId eq customerId }
            .andWhere { OrderTable.id eq orderId }
            .orWhere { OrderTable.status eq OrderStatus.CONFIRMED.name.lowercase() }
            .orWhere { OrderTable.status eq OrderStatus.DELIVERED.name.lowercase() }
            .orWhere { OrderTable.status eq OrderStatus.CANCELED.name.lowercase() }
            .singleOrNull() ?: throw CommonException("Cliente ou encomenda não existe")

        val order = OrderEntity.wrapRow(query)
        val store = StoreEntity.findById(order.storeId)!!
        val productItems = OrderItemEntity.find { OrderItemTable.orderId eq order.id }.map {
            CustomerOrderItemResponse(
                it.productName,
                store.name,
                it.color,
                it.size,
                it.quantity,
                it.price
            )
        }

        CustomerOrderResponse(
            order.id.value,
            order.quantity,
            order.total,
            order.status,
            order.deliveryMethod,
            order.addressName,
            order.createdAt.toString(),
            productItems
        )
    }

    override suspend fun getOrdersByCustomerId(customerId: Int): List<OrderResponse> = query {
        val query = OrderTable.selectAll()
            .andWhere { OrderTable.customerId eq customerId }
            .orWhere { OrderTable.status eq OrderStatus.PROCESSING.name.lowercase() }
            .orWhere { OrderTable.status eq OrderStatus.DELIVERED.name.lowercase() }
            .orWhere { OrderTable.status eq OrderStatus.CANCELED.name.lowercase() }

        OrderEntity.wrapRows(query).map { it.response() }
    }

    override suspend fun getOrderById(orderId: Int): OrderResponse = query {
        OrderEntity.findById(orderId)?.let {
            it.response()
        } ?: throw orderId.notFoundException()
    }

    override suspend fun updateOrderStatus(
        storeId: Int,
        orderId: Int,
        orderStatus: OrderStatusUpdateBody
    ): OrderResponse = query {
        val orderEntity =
            OrderEntity.find { (OrderTable.id eq orderId) and (OrderTable.storeId eq storeId) }.toList().singleOrNull()
        orderEntity?.let {
            it.status = orderStatus.status
            it.response()
        } ?: throw orderId.notFoundException()
    }
}