package com.piashcse.features.store.routes

import com.piashcse.features.order.controllers.OrderController
import com.piashcse.features.product.controllers.ProductController
import com.piashcse.features.product.controllers.ReviewController
import com.piashcse.features.product.models.product.request.AddProductBody
import com.piashcse.features.product.models.product.request.UpdateProductBody
import com.piashcse.features.product.models.product.response.ProductResponse
import com.piashcse.features.product.models.product.response.ProductWithVariationResponse
import com.piashcse.features.product.models.review.response.ReviewResponse
import com.piashcse.features.store.controllers.StoreController
import com.piashcse.features.store.models.response.StoreOrderResponse
import com.piashcse.features.store.models.response.StoreResponse
import com.piashcse.plugins.RoleManagement
import com.piashcse.shared.authentication.request.LoginBody
import com.piashcse.shared.authentication.response.LoginResponse
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.AppConstants
import com.piashcse.utils.extension.apiResponse
import com.piashcse.utils.extension.save
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.storeRoute(
    storeController: StoreController,
    orderController: OrderController,
    productController: ProductController,
    reviewController: ReviewController
) {
    route("store") {


        post("login", {
            tags("Store")
            request {
                body<LoginBody>()
            }
            apiResponse<LoginResponse<StoreResponse>>()
        }) {
            val requestBody = call.receive<LoginBody>()
            requestBody.validation()

            call.respond(
                ApiResponse.success(
                    storeController.login(requestBody),
                    HttpStatusCode.OK
                )
            )
        }


        post("image-store", {
            tags("Store")
            summary = "Upload store Logo"
            description = "The image file to upload"

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
            apiResponse<StoreResponse>()
        })
        {
            val multipartData = call.receiveMultipart()
            var imageUrl: String? = null
            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "image") {
                            imageUrl = part.value

                        }
                    }

                    is PartData.FileItem -> {
                        val fileName = part.save(
                            AppConstants.Image.RESOURCES_PATH +
                                    AppConstants.Image.STORE_PATH
                        )
                        imageUrl = AppConstants.Image.EXTERNAL_PATH +
                                AppConstants.Image.STORE_PATH + fileName
                    }

                    else -> {}
                }
                part.dispose()
            }
            val updatedStore = storeController.updateStoreLogo(1, imageUrl!!)
            call.respond(
                ApiResponse.success(
                    updatedStore,
                    HttpStatusCode.OK
                )
            )
        }


        // Order

        get("orders", {
            tags("Store")
            summary = "Return all order of the store"
            apiResponse<List<StoreOrderResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    orderController.getOrdersByStoreId(1),
                    HttpStatusCode.OK
                )
            )
        }

        get("orders/{id}", {
            tags("Store")
            summary = "Return store order with given id"
            request {
                pathParameter<Int>("id") {
                    required = true
                    description = "order id"
                }
            }
            apiResponse<StoreOrderResponse>()
        }) {
            val id = call.pathParameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    orderController.getOrderByStoreId(1, id.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

        // Product
        get("products", {
            tags("Store")
            summary = "Return all products of the store"
            request {

            }
            apiResponse<List<ProductResponse>>()
        }) {
            call.respond(
                ApiResponse.success(
                    productController.getStoreProducts(1),
                    HttpStatusCode.OK
                )
            )
        }

        post("products", {
            tags("Store")
            summary = "Return add new product to the store"
            request {
                body<AddProductBody> {
                    description = "Required parameter for new product"
                }

            }
            apiResponse<ProductWithVariationResponse>()
        }) {
            val productBody = call.receive<AddProductBody>()
            productBody.validation()
            call.respond(
                ApiResponse.success(
                    productController.addProduct(1, productBody),
                    HttpStatusCode.OK
                )
            )
        }

        put("products/{id}", {
            tags("Store")
            summary = "Update the product of the store with given id"
            request {
                body<UpdateProductBody>()
                pathParameter<String>("id") {
                    required = true
                    description = "Product id"
                }
            }
            apiResponse<ProductWithVariationResponse>()
        }) {
            val productId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val productBody = call.receive<UpdateProductBody>()
            productBody.validation()
            call.respond(
                ApiResponse.success(
                    productController.updateProduct(1, productId.toInt(), productBody),
                    HttpStatusCode.OK
                )
            )
        }

        get("products/{id}", {
            tags("Store")
            summary = "Return the product of the store with given id"
            request {
                pathParameter<Int>("id") {
                    required = true
                    description = "product id"
                }

            }
            apiResponse<ProductWithVariationResponse>()
        }) {
            val productId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    productController.getProductByStoreId(1, productId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }
        get("products/{id}/reviews", {
            tags("Store")
            summary = "Return reviews of the given product id"
            request {
                pathParameter<Int>("id") {
                    required = true
                    description = "Product id"
                }
            }
            apiResponse<List<ReviewResponse>>()
        }) {
            val productId = call.request.queryParameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(
                ApiResponse.success(
                    reviewController.getProductReviews(productId.toInt()),
                    HttpStatusCode.OK
                )
            )
        }

    }
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
//  }
//}