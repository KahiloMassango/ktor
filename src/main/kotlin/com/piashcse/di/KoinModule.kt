package com.piashcse.di

import com.piashcse.features.admin.controllers.AdminController
import com.piashcse.features.customer.controllers.CustomerController
import com.piashcse.features.order.controllers.DeliveryController
import com.piashcse.features.order.controllers.OrderController
import com.piashcse.features.payment.controllers.PaymentController
import com.piashcse.features.product.controllers.*
import com.piashcse.features.store.controllers.StoreController
import org.koin.dsl.module

val controllerModule = module {

    single { OrderController() }
    single { ProductController() }
    single { CategoryController() }
    single { SubCategoryController() }
    single { DeliveryController() }
    single { AdminController() }
    single { StoreController() }
    single { ProductController() }
    single { CustomerController() }
    single { PaymentController() }
    single { ReviewController() }
    single { VariationController() }
    single { VariationOptionController() }

}