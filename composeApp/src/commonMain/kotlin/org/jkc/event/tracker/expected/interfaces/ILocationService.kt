package org.jkc.event.tracker.expected.interfaces

import org.jkc.event.tracker.domain.entity.LocationEntity

interface ILocationService {
    suspend fun getCurrentLocation(): LocationEntity?
}
