package com.piashcse.plugins

import com.piashcse.features.admin.models.request.AdminRegistrationBody
import com.piashcse.features.admin.models.request.AdminUpdateBody
import com.piashcse.features.customer.models.request.CostumerRegistrationBody
import com.piashcse.features.customer.models.request.CustomerUpdateBody
import com.piashcse.features.order.models.delivery.request.DeliveryStatusUpdate
import com.piashcse.features.order.models.order.request.AddOrderBody
import com.piashcse.features.order.models.order.request.OrderProductItem
import com.piashcse.features.order.models.order.request.OrderStatusUpdateBody
import com.piashcse.features.payment.models.request.AddPaymentBody
import com.piashcse.features.payment.models.request.PaymentStatusUpdate
import com.piashcse.features.product.models.category.request.AddCategoryBody
import com.piashcse.features.product.models.category.request.AddSubCategoryBody
import com.piashcse.features.product.models.category.request.CategoryUpdateBody
import com.piashcse.features.product.models.category.request.SubCategoryUpdateBody
import com.piashcse.features.product.models.product.request.AddProductBody
import com.piashcse.features.product.models.product.request.AddProductConfigurationBody
import com.piashcse.features.product.models.product.request.ProductSearch
import com.piashcse.features.product.models.product.request.UpdateProductBody
import com.piashcse.features.product.models.review.request.AddReviewBody
import com.piashcse.features.product.models.variation.request.AddVariationBody
import com.piashcse.features.product.models.variation.request.AddVariationOptionBody
import com.piashcse.features.product.models.variation.request.VariationOptionUpdateBody
import com.piashcse.features.product.models.variation.request.VariationUpdateBody
import com.piashcse.features.store.models.request.StoreRegistrationBody
import com.piashcse.features.store.models.request.StoreStatusUpdate
import com.piashcse.features.store.models.request.StoreUpdateBody
import com.piashcse.shared.authentication.request.ChangePasswordBody
import com.piashcse.shared.authentication.request.LoginBody
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        // Authentication
        validate<LoginBody> { login ->
            login.validation()
            ValidationResult.Valid
        }
        validate<ChangePasswordBody> { changePasswordBody ->
            changePasswordBody.validation()
            ValidationResult.Valid
        }
        // Admin
        validate<AdminRegistrationBody> { adminRegistration ->
            adminRegistration.validation()
            ValidationResult.Valid
        }
        validate<AdminUpdateBody> { adminUpdate ->
            adminUpdate.validation()
            ValidationResult.Valid
        }
        // Costumer
        validate<CostumerRegistrationBody> { costumerRegistration ->
            costumerRegistration.validation()
            ValidationResult.Valid
        }
        validate<CustomerUpdateBody> { costumerUpdateBody ->
            costumerUpdateBody.validation()
            ValidationResult.Valid
        }
        // Store
        validate<StoreRegistrationBody> { body ->
            body.validation()
            ValidationResult.Valid
        }
        // Login
        validate<LoginBody> { login ->
            login.validation()
            ValidationResult.Valid
        }
        // Product
        validate<ProductSearch> { search ->
            search.validation()
            ValidationResult.Valid
        }
        validate<UpdateProductBody> { updateProduct ->
            updateProduct.validation()
            ValidationResult.Valid
        }
        validate<AddProductBody> { productBody ->
            productBody.validation()
            ValidationResult.Valid
        }
        // Category
        validate<AddCategoryBody> { addCategoryBody ->
            addCategoryBody.validation()
            ValidationResult.Valid
        }
        validate<AddSubCategoryBody> { productSubCategory ->
            productSubCategory.validation()
            ValidationResult.Valid
        }
        validate<CategoryUpdateBody> { categoryUpdate ->
            categoryUpdate.validation()
            ValidationResult.Valid
        }
        validate<SubCategoryUpdateBody> { subCategoryUpdate ->
            subCategoryUpdate.validation()
            ValidationResult.Valid
        }
        // Review
        validate<AddReviewBody> { review ->
            review.validation()
            ValidationResult.Valid
        }
        // Order
        validate<AddOrderBody> { orderBody ->
            orderBody.validation()
            ValidationResult.Valid
        }
        validate<OrderStatusUpdateBody> { orderBody ->
            orderBody.validation()
            ValidationResult.Valid
        }
        validate<OrderProductItem> { orderItem ->
            orderItem.validation()
            ValidationResult.Valid
        }
        // Delivery
        validate<DeliveryStatusUpdate> { delivery ->
            delivery.validation()
            ValidationResult.Valid
        }
        //Payment
        validate<AddPaymentBody> { payment ->
            payment.validation()
            ValidationResult.Valid
        }
        validate<PaymentStatusUpdate> { payment ->
            payment.validation()
            ValidationResult.Valid
        }
        // Store
        validate<StoreRegistrationBody> { store ->
            store.validation()
            ValidationResult.Valid
        }
        validate<StoreUpdateBody> { store ->
            store.validation()
            ValidationResult.Valid
        }
        validate<StoreStatusUpdate>  {
            it.validation()
            ValidationResult.Valid
        }
        // Variation
        validate<AddVariationBody> {
            it.validation()
            ValidationResult.Valid
        }
        validate<AddVariationOptionBody> {
            it.validation()
            ValidationResult.Valid
        }
        validate<VariationUpdateBody> {
            it.validation()
            ValidationResult.Valid
        }
        validate< VariationOptionUpdateBody> {
            it.validation()
            ValidationResult.Valid
        }
        // Product
        validate<AddProductConfigurationBody> {
            it.validation()
            ValidationResult.Valid
        }
    }
}