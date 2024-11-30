package com.piashcse.utils

object AppConstants {
    object SuccessMessage {
        object Password {
            const val PASSWORD_CHANGE_SUCCESS = "Password change successful"
        }

        object VerificationCode {
            const val VERIFICATION_CODE_SENT_TO = "verification code sent to"
            const val VERIFICATION_CODE_IS_NOT_VALID = "Verification code is not valid"
        }
    }

    object DataBaseTransaction {
        const val FOUND = 1
        const val NOT_FOUND = 2
    }

    object SmtpServer {
        const val HOST_NAME = "smtp.googlemail.com"
        const val PORT = 465
        const val DEFAULT_AUTHENTICATOR = "smtp@gmail.com" // your smtp email address
        const val DEFAULT_AUTHENTICATOR_PASSWORD = "smtpcredential" // password for smtp
        const val EMAIL_SUBJECT = "Forget Password"
        const val SENDING_EMAIL = "sendingemail.@gmail.com" // The email from where it will send to user
    }

    object Image {
        const val EXTERNAL_PATH = "localhost:8080/static/"
        const val RESOURCES_PATH = "src/main/resources/"
        const val STORE_PATH = "store-images/"
        const val CUSTOMER_PATH = "customer-images/"
        const val PRODUCT_PATH = "product-images/"
    }
}