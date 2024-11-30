package com.piashcse.plugins

import com.piashcse.features.admin.controllers.AdminController
import com.piashcse.features.admin.routes.adminRoute
import com.piashcse.features.customer.controllers.CustomerController
import com.piashcse.features.customer.routes.customerRoute
import com.piashcse.features.order.controllers.DeliveryController
import com.piashcse.features.order.controllers.OrderController
import com.piashcse.features.payment.controllers.PaymentController
import com.piashcse.features.product.controllers.*
import com.piashcse.features.product.routes.*
import com.piashcse.features.store.controllers.StoreController
import com.piashcse.features.store.routes.storeRoute
import com.piashcse.utils.AppConstants
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRoute() {
    val adminController: AdminController by inject()
    val storeController: StoreController by inject()
    val customerController: CustomerController by inject()
    val categoryController: CategoryController by inject()
    val subCategoryController: SubCategoryController by inject()
    val productController: ProductController by inject()
    val orderController: OrderController by inject()
    val paymentController: PaymentController by inject()
    val reviewController: ReviewController by inject()
    val deliveryController: DeliveryController by inject()
    val variationController: VariationController by inject()
    val variationOptionController: VariationOptionController by inject()
    routing {
        adminRoute(adminController, storeController, customerController, orderController, deliveryController)
        customerRoute(customerController, orderController)
        storeRoute(storeController, orderController, productController, reviewController)
        productRoute(productController)
        reviewRatingRoute(reviewController)
        categoryRoute(categoryController, subCategoryController)
        subCategoryRoute(subCategoryController)
        variationRoute(variationController)
        variationOptionRoute(variationOptionController)

        staticFiles("/static", File(AppConstants.Image.RESOURCES_PATH))
    }
}
