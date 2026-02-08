package org.jkc.event.tracker.domain.entity

data class CategoryEntity(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String,
    val order: Int,
)