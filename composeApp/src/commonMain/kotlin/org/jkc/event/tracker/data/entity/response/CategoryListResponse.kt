package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable
import org.jkc.event.tracker.domain.entity.CategoryEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Serializable
class CategoryListResponse(
    val success: Boolean,
    val data: List<Category>,
    val pagination: Pagination
){
    @Serializable
    data class Category(
        val id: Int,
        val name: String,
        val icon: String,
        val position: Int,
        val status: String,
        val createdAt: String,
        val updatedAt: String
    )

    @Serializable
    data class Pagination(
        val total: Int,
        val page: Int,
        val limit: Int,
        val totalPages: Int
    )
}

fun List<CategoryListResponse.Category>.toEntity(): List<CategoryEntity> {
    return this.map {
        CategoryEntity(
            id = it.id,
            name = it.name,
            icon = it.icon,
            position = it.position,
            status = it.status,
            createdAt = Instant.parse(it.createdAt).toLocalDateTime(TimeZone.UTC),
            updatedAt = Instant.parse(it.updatedAt).toLocalDateTime(TimeZone.UTC)
        )
    }
}