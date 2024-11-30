package com.piashcse.features.customer.routes

import com.piashcse.features.customer.controllers.CustomerController
import com.piashcse.features.customer.entities.CustomerResponse
import com.piashcse.features.customer.models.request.CostumerRegistrationBody
import com.piashcse.features.customer.models.request.CustomerUpdateBody
import com.piashcse.features.customer.models.response.CostumerRegistrationResponse
import com.piashcse.features.order.controllers.OrderController
import com.piashcse.features.order.models.order.request.AddOrderBody
import com.piashcse.features.order.models.order.response.CustomerOrderResponse
import com.piashcse.features.order.models.order.response.OrderResponse
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.AppConstants
import com.piashcse.utils.extension.apiResponse
import com.piashcse.utils.extension.save
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.customerRoute(
    customerController: CustomerController,
    orderController: OrderController

) {
    route("customer") {
        post("login", {
            tags("Customer")
            summary = "Customer login"
            request {
                body<LoginBody>()
            }
            apiResponse<LoginResponse<CustomerResponse>>()
        }) {
            val requestBody = call.receive<LoginBody>()
            requestBody.validation()
            call.respond(
                ApiResponse.success(
                    customerController.login(requestBody),
                    HttpStatusCode.OK
                )
            )
        }
        post("registration", {
            tags("Customer")
            summary = "Registry signin"
            request {
                body<CostumerRegistrationBody>()
            }
            apiResponse<CostumerRegistrationResponse>()
        }) {
            val requestBody = call.receive<CostumerRegistrationBody>()
            requestBody.validation()
            call.respond(
                ApiResponse.success(
                    customerController.addCustomer(requestBody),
                    HttpStatusCode.OK
                )
            )
        }

        post({
            tags("Customer")
            summary = "Update customer information"
            request {
                body<CustomerUpdateBody> {
                    description = "Customer update parameters"
                }
            }
            apiResponse<CustomerResponse>()
        }) {
            val customerUpdate = call.receive<CustomerUpdateBody>()
            customerUpdate.validation()

            call.respond(
                ApiResponse.success(
                    customerController.updateCustomer(1, customerUpdate),
                    HttpStatusCode.OK
                )
            )
        }

        post("logo-upload", {
            tags("Customer")
            summary = "Upload customer profile photo"
            request {
                multipartBody {
                    mediaTypes = setOf(ContentType.MultiPart.FormData)
                    part<File>("image") {
                        mediaTypes(
                            setOf(
                                ContentType.Image.PNG,
                                ContentType.Image.JPEG
                            )
                        )
                    }
                }
            }
            apiResponse<CustomerResponse>()
        }) {

            val multipartData = call.receiveMultipart()
            var imageUrl: String? = null

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val fileName = part.save(
                            AppConstants.Image.RESOURCES_PATH +
                                    AppConstants.Image.CUSTOMER_PATH
                        )
                        imageUrl = AppConstants.Image.EXTERNAL_PATH +
                                AppConstants.Image.CUSTOMER_PATH + fileName
                    }

                    else -> {}
                }
                part.dispose()
            }
            call.respond(
                ApiResponse.success(
                    customerController.updateCustomerProfileImage(1, imageUrl!!),
                    HttpStatusCode.OK
                )
            )
        }

        // Order
        post("order", {
            tags("Customer")
            summary = "Customer request an order"
            request {
                body<AddOrderBody>()
            }
            apiResponse<OrderResponse>()
        }) {
            val orderBody = call.receive<AddOrderBody>()
            orderBody.validation()
            call.respond(
                ApiResponse.success(
                    orderController.addOrder(1, orderBody),
                    HttpStatusCode.OK
                )
            )

        }
        get("order", {
            tags("Customer")
            summary = "Get customer orders"
            apiResponse<List<OrderResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    orderController.getOrdersByCustomerId(1),
                    HttpStatusCode.OK
                )
            )

        }
        get("order/{id}", {
            tags("Customer")
            summary = "Get order with given id"
            request {
                pathParameter<Int>("id") {
                    required = true
                    description = "Order id"
                }
            }
            apiResponse<CustomerOrderResponse>()
        }) {
            val orderId = call.pathParameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    orderController.getOrderByCustomerId(1, orderId.toInt()),
                    HttpStatusCode.OK
                )
            )

        }


        /*get("verify-password-change", {
            tags("User")
            request {
                queryParameter<String>("email") {
                    required = true
                }
                queryParameter<String>("verificationCode") {
                    required = true
                }
                queryParameter<String>("newPassword") {
                    required = true
                }
            }
            apiResponse()
        }) {
            val (email, verificationCode, newPassword) = call.requiredParameters(
                "email", "verificationCode", "newPassword"
            ) ?: return@get

            UserController().forgetPasswordVerificationCode(
                ConfirmPassword(
                    email, verificationCode, newPassword
                )
            ).let {
                when (it) {
                    AppConstants.DataBaseTransaction.FOUND -> {
                        call.respond(
                            ApiResponse.success(
                                AppConstants.SuccessMessage.Password.PASSWORD_CHANGE_SUCCESS, HttpStatusCode.OK
                            )
                        )
                    }

                    AppConstants.DataBaseTransaction.NOT_FOUND -> {
                        call.respond(
                            ApiResponse.success(
                                AppConstants.SuccessMessage.VerificationCode.VERIFICATION_CODE_IS_NOT_VALID,
                                HttpStatusCode.OK
                            )
                        )
                    }
                }
            }
        }*/
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