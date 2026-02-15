package org.jkc.event.tracker.domain.entity

data class SubCategoryEntity(
    val id: Int,
    val name: String,
    val categoryId: String?,
    val order: Int,
)