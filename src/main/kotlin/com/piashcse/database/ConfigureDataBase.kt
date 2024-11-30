package com.piashcse.database

import com.piashcse.features.admin.entities.AdminTable
import com.piashcse.features.customer.entities.CustomerTable
import com.piashcse.features.order.entities.DeliveryTable
import com.piashcse.features.order.entities.OrderItemTable
import com.piashcse.features.order.entities.OrderTable
import com.piashcse.features.payment.entities.PaymentTable
import com.piashcse.features.product.entities.*
import com.piashcse.features.store.entities.StoreTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource


fun configureDataBase() {
    initDB()
    transaction {
        addLogger(StdOutSqlLogger)
        create(
            // New
            AdminTable,
            CustomerTable,
            StoreTable,
            // product
            ProductTable,
            ProductItemTable,
            VariationTable,
            VariationOptionTable,
            ProductConfigurationTable,
            ProductImageTable,
            CategoryTable,
            SubCategoryTable,
            ReviewTable,


            OrderTable,
            OrderItemTable,
            DeliveryTable,
            PaymentTable,

        )
    }
}

private fun initDB() {
    // database connection is handled from hikari properties
    val config = HikariConfig("/hikari.properties")
    val dataSource = HikariDataSource(config)
    runFlyway(dataSource)
    Database.connect(dataSource)
}

private fun runFlyway(datasource: DataSource) {
    val flyway = Flyway.configure().dataSource(datasource).load()
    try {
        flyway.info()
        flyway.migrate()
    } catch (e: Exception) {
        throw e
    }
}