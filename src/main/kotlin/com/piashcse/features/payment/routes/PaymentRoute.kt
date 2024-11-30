package com.piashcse.features.payment.routes

import com.piashcse.features.payment.controllers.PaymentController
import com.piashcse.features.payment.models.request.AddPaymentBody
import com.piashcse.features.payment.models.request.PaymentStatusUpdate
import com.piashcse.features.payment.models.response.PaymentResponse
import com.piashcse.plugins.RoleManagement
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.extension.apiResponse
import io.github.smiley4.ktorswaggerui.dsl.routing.put
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.paymentRoute(paymentController: PaymentController) {
    route("payment") {
        authenticate(RoleManagement.CUSTOMER.role) {
            post({
                tags("Payment")
                request {
                    body<AddPaymentBody>()
                }
                apiResponse<PaymentResponse>()
            }) {
                val requestBody = call.receive<AddPaymentBody>()
                call.respond(
                    ApiResponse.success(
                        paymentController.addPayment(requestBody), HttpStatusCode.OK
                    )
                )
            }
            get("{id}", {
                tags("Payment")
                request {
                    pathParameter<String>("id") {
                        required = true
                    }
                }
                apiResponse<PaymentResponse>()
            }) {
                val paymentId = call.parameters["id"]?.toInt() ?: return@get
                call.respond(
                    ApiResponse.success(
                        paymentController.findPaymentBy(paymentId), HttpStatusCode.OK
                    )
                )
            }
            put("{id}/status", {
                tags("Payment")
                request {
                    pathParameter<String>("id") {
                        required = true
                    }
                    body<PaymentStatusUpdate>()
                }
                apiResponse<PaymentResponse>()
            }) {
                val paymentId = call.parameters["id"]?.toInt() ?: return@put
                val paymentStatus = call.receive<PaymentStatusUpdate>()
                call.respond(
                    ApiResponse.success(
                        paymentController.updatePaymentStatus(paymentId, paymentStatus),
                        HttpStatusCode.OK
                    )
                )
            }
        }
    }
}