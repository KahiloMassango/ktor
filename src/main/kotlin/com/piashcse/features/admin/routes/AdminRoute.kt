package com.piashcse.features.admin.routes

import com.piashcse.features.admin.controllers.AdminController
import com.piashcse.features.admin.models.request.AdminRegistrationBody
import com.piashcse.features.admin.models.response.AdminResponse
import com.piashcse.features.customer.controllers.CustomerController
import com.piashcse.features.customer.entities.CustomerResponse
import com.piashcse.features.order.controllers.DeliveryController
import com.piashcse.features.order.controllers.OrderController
import com.piashcse.features.order.models.delivery.response.DeliveryResponse
import com.piashcse.features.order.models.order.response.OrderResponse
import com.piashcse.features.store.controllers.StoreController
import com.piashcse.features.store.models.request.StoreStatusUpdate
import com.piashcse.features.store.models.response.StoreResponse
import com.piashcse.features.store.models.response.StoreStatusResponse
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.extension.apiResponse
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoute(
    adminController: AdminController,
    storeController: StoreController,
    customerController: CustomerController,
    orderController: OrderController,
    deliveryController: DeliveryController
) {
    route("administration") {
        post("login", {
            tags("Administration")
            request {
                body<LoginBody>()
            }
            apiResponse<LoginResponse<AdminResponse>>()
        }) {
            val requestBody = call.receive<LoginBody>()
            requestBody.validation()
            call.respond(
                ApiResponse.success(
                    adminController.login(requestBody),
                    HttpStatusCode.OK
                )
            )
        }
        post("registration", {
            tags("Administration")
            request {
                body<AdminRegistrationBody>()
            }
            apiResponse<AdminResponse>()
        }) {
            val requestBody = call.receive<AdminRegistrationBody>()
            requestBody.validation()
            call.respond(
                ApiResponse.success(
                    adminController.addAdmin(requestBody),
                    HttpStatusCode.OK
                )
            )
        }
        // Store
        get("stores", {
            tags("Administration")

            apiResponse<List<StoreResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    storeController.getAllStores(),
                    HttpStatusCode.OK
                )
            )
        }

        put("stores/{id}/status", {
            tags("Administration")
            request {
                pathParameter<Int>("id")
                body<StoreStatusUpdate>()

            }
            apiResponse<StoreStatusResponse>()
        }) {
            val storeId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val status = call.receive<StoreStatusUpdate>()
            status.validation()
            call.respond(
                ApiResponse.success(
                    storeController.updateStoreStatus(storeId.toInt(), status.isActive),
                    HttpStatusCode.OK
                )
            )
        }

        // Customer
        get("customers", {
            tags("Administration")
            apiResponse<List<CustomerResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    customerController.getAllCostumers(),
                    HttpStatusCode.OK
                )
            )
        }

        get("customers/{id}", {
            tags("Administration")
            request {
                pathParameter<Int>("id")
            }
            apiResponse<CustomerResponse>()
        }) {
            val customerId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    customerController.getCustomerById(customerId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

        // Order
        get("orders", {
            tags("Administration")
            summary = "Return all orders"
            apiResponse<List<OrderResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    orderController.getOrders(),
                    HttpStatusCode.OK
                )
            )
        }

        get("orders/store/{id}", {
            tags("Administration")
            summary = "Return orders of the given store id"
            request {
                pathParameter<Int>("id") {
                    description = "Store id"
                }
            }
            apiResponse<List<OrderResponse>>()
        }) {
            val storeId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    orderController.getOrdersByStoreId(storeId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

        get("orders/{id}", {
            tags("Administration")
            summary = "Return Order with given id"
            request {
                pathParameter<Int>("id") {
                    description = "Order id"
                }
            }
            apiResponse<OrderResponse>()
        }) {
            val orderId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    orderController.getOrderById(orderId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

        // Delivery
        get("delivery", {
            tags("Administration")
            summary = "Return all deliveries"
            apiResponse<List<DeliveryResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    deliveryController.getAllDelivers(),
                    HttpStatusCode.OK
                )
            )
        }

        get("delivery/store/{id}", {
            tags("Administration")
            summary = "Return deliveries of the given store id"
            request {
                pathParameter<Int>("id") {
                    description = "Store id"
                }
            }
            apiResponse<List<DeliveryResponse>>()
        }) {
            val storeId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    deliveryController.getDeliveriesByStoreId(1),
                    HttpStatusCode.OK
                )
            )
        }

        get("orders/{id}", {
            tags("Administration")
            summary = "Return delivery with given id"
            request {
                pathParameter<Int>("id") {
                    description = "delivery id"
                }
            }
            apiResponse<DeliveryResponse>()
        }) {
            val deliveryId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    deliveryController.getDeliveryById(deliveryId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }


        /* authenticate(RoleManagement.ADMIN.role, RoleManagement.STORE.role, RoleManagement.CUSTOMER.role) {
             put("change-password", {
                 tags("User")
                 protected = true
                 request {
                     queryParameter<String>("oldPassword") {
                         required = true
                     }
                     queryParameter<String>("newPassword") {
                         required = true
                     }
                 }
                 apiResponse()
             }) {
                 val (oldPassword, newPassword) = call.requiredParameters("oldPassword", "newPassword") ?: return@put
                 val loginUser = call.principal<JwtTokenBody>()
                 userController.changePassword(loginUser?.userId!!, ChangePassword(oldPassword, newPassword)).let {
                     if (it) call.respond(
                         ApiResponse.success(
                             "Password has been changed", HttpStatusCode.OK
                         )
                     ) else call.respond(
                         ApiResponse.failure(
                             "Old password is wrong", HttpStatusCode.OK
                         )
                     )
                 }
             }
         }*/
    }
}