package org.jkc.event.tracker.domain.entity

import kotlinx.datetime.LocalDateTime

data class CategoryEntity(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String,
    val icon: String,
    val position: Int,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)