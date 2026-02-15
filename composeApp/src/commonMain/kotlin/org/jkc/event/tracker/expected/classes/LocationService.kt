package org.jkc.event.tracker.expected.classes

import org.jkc.event.tracker.domain.entity.LocationEntity
import org.jkc.event.tracker.expected.interfaces.ILocationService

expect class LocationService() : ILocationService {
    override suspend fun getCurrentLocation(): LocationEntity?
}
