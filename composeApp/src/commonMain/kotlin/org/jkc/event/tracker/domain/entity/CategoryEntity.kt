package org.jkc.event.tracker.domain.entity

import kotlinx.datetime.LocalDateTime

data class CategoryEntity(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String,
    val order: Int,
)