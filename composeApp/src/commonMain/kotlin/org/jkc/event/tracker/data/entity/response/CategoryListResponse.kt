package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable
import org.jkc.event.tracker.domain.entity.CategoryEntity

import kotlin.Int

@Serializable
data class CategoryResponse(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String,
    val order: Int,
)

fun List<CategoryResponse>.toEntity(): List<CategoryEntity> {
    return this.map {
        CategoryEntity(
            id = it.id,
            name = it.name,
            description = it.description.orEmpty(),
            imageUrl = it.imageUrl,
            order = it.order
        )
    }
}