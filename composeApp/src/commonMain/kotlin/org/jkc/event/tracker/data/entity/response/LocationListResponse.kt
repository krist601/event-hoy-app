package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable
import org.jkc.event.tracker.domain.entity.LocationEntity


@Serializable
data class LocationResponse(
    val id: Int,
    val name: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val url: String?
)

fun List<LocationResponse>.toEntity(): List<LocationEntity> {
    return this.map {
        LocationEntity(
            id = it.id,
            name = it.name,
            address = it.address.orEmpty(),
            latitude = it.latitude,
            longitude = it.longitude,
            url = it.url
        )
    }
}