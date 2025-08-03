package org.jkc.event.tracker.domain.entity

data class EventEntity(
    val id: Int,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String?,
    val image: String,
    val externalUrl: String?,
    val source: String?,
    val priceFrom: String?,
    val featured: Boolean,
    val ticketSaleStart: String?,
    val ticketSaleEnd: String?,
    val status: String,
    val categoryId: Int,
    val venueId: Int,
    val createdAt: String,
    val updatedAt: String,
)