package org.jkc.event.tracker.domain.entity

data class LocationEntity(
    val id: Int,
    val name: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val url: String?,
)