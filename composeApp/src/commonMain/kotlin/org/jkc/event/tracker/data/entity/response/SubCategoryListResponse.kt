package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable
import org.jkc.event.tracker.domain.entity.SubCategoryEntity

@Serializable
data class SubCategoryResponse(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val order: Int
)

fun List<SubCategoryResponse>.toEntity(): List<SubCategoryEntity> {
    return this.map {
        SubCategoryEntity(
            id = it.id,
            name = it.name,
            categoryId = it.categoryId.toString(),
            order = it.order
        )
    }
}
