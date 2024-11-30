package com.piashcse.shared.entities

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.time.ZoneOffset

abstract class BaseIntIdTable(name: String) : IntIdTable(name) {
    val createdAt = datetime("created_at").clientDefault { currentUtc() }
    val updatedAt = datetime("updated_at").nullable()
}

abstract class BaseIntEntity(id: EntityID<Int>, table: BaseIntIdTable) : Entity<Int>(id) {
    val createdAt by table.createdAt
    var updatedAt by table.updatedAt
}
abstract class BaseIntEntityClass<E : BaseIntEntity>(table: BaseIntIdTable) : EntityClass<Int, E>(table){
    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                try {
                    action.toEntity(this)?.updatedAt = currentUtc()
                } catch (e: Exception) {
                    //nothing much to do here
                }
            }
        }
    }
}
// generating utc time
fun currentUtc(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)

