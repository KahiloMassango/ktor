package com.piashcse.utils.extension

import com.piashcse.features.store.models.response.StoreStatusResponse
import com.piashcse.shared.autorization.JwtTokenBody
import com.piashcse.utils.ApiResponse
import com.piashcse.utils.CommonException
import com.piashcse.utils.Response
import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

fun String.notFoundException(): CommonException {
    return CommonException("$this is not Exist")
}

fun Int.notFoundException(msg: String = ""): CommonException {
    return CommonException("$this $msg")
}

fun Int.alreadyExistException(secondaryInfo: String = ""): CommonException {
    return if (secondaryInfo.isEmpty()) CommonException("$this is already Exist")
    else CommonException("$this $secondaryInfo is already Exist")
}
fun String.alreadyExistException(secondaryInfo: String = ""): CommonException {
    return if (secondaryInfo.isEmpty()) CommonException("$this is already Exist")
    else CommonException("$this $secondaryInfo")
}

inline fun <reified T> OpenApiRoute.apiResponse() {
    return response {
        HttpStatusCode.OK to {
            description = "Successful"
            body<Response<T>> {
                mediaTypes = setOf(ContentType.Application.Json)
                description = "Successful"
            }
        }
        HttpStatusCode.InternalServerError
    }
}

suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction {
        block()
    }
}

fun ApplicationCall.currentUser(): JwtTokenBody {
    return this.principal<JwtTokenBody>() ?: throw IllegalStateException("No authenticated user found")
}

suspend fun ApplicationCall.requiredParameters(vararg requiredParams: String): List<String>? {
    val missingParams = requiredParams.filterNot { this.parameters.contains(it) }
    if (missingParams.isNotEmpty()) {
        this.respond(ApiResponse.success("Missing parameters: $missingParams", HttpStatusCode.BadRequest))
        return null
    }
    return requiredParams.map { this.parameters[it]!! }
}