package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable
import org.jkc.event.tracker.domain.entity.CategoryEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Serializable
class CategoryListResponse(
    val data: List<Category>,
    val pagination: Pagination
){
    @Serializable
    data class Category(
        val id: Int,
        val name: String,
        val slug: String,
        val description: String?,
        val icon: String,
        val position: Int,
        val isActive: Boolean,
        val createdAt: String,
        val updatedAt: String
    )

    @Serializable
    data class Pagination(
        val page: Int,
        val limit: Int,
        val total: Int,
        val totalPages: Int,
        val hasNext: Boolean,
        val hasPrev: Boolean
    )
}

fun List<CategoryListResponse.Category>.toEntity(): List<CategoryEntity> {
    return this.map {
        CategoryEntity(
            id = it.id,
            name = it.name,
            slug = it.slug,
            description = it.description.orEmpty(),
            icon = it.icon,
            position = it.position,
            isActive = it.isActive,
            createdAt = Instant.parse(it.createdAt).toLocalDateTime(TimeZone.UTC),
            updatedAt = Instant.parse(it.updatedAt).toLocalDateTime(TimeZone.UTC)
        )
    }
}