package com.piashcse.features.store.models.request

import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class StoreStatusUpdate(
    val isActive: Boolean
) {
    fun validation() {
        validate(this) {
            validate(StoreStatusUpdate::isActive).isNotNull()
        }
    }
}
