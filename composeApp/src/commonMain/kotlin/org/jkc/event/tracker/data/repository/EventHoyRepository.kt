package org.jkc.event.tracker.data.repository

import org.jkc.event.tracker.data.datasource.api.IAPIDataSource
import org.jkc.event.tracker.data.datasource.local.ILocalDataSource
import org.jkc.event.tracker.data.entity.response.toEntity
import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.entity.LocationEntity

class EventHoyRepository(
    private val apiDataSource: IAPIDataSource,
    private val localDataSource: ILocalDataSource
) {
    suspend fun getEventList(
        text: String? = null,
        type: String? = null,
        page: Int? = null,
        category: String? = null,
        location: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Int? = null
    ): Pair<List<EventEntity>, Boolean> {
        try {
            val data = apiDataSource.getEventList(
                text = text,
                type = type,
                page = page,
                category = category,
                location = location,
                startDate = startDate,
                endDate = endDate,
                latitude = latitude,
                longitude = longitude,
                radius = radius
            )
            return Pair(
                data.content.toEntity(),
                data.pagination?.hasNext ?: false
            )
        } catch (e: Exception) {
            try {
                val storedEventList = localDataSource.getEventList(
                    text = text,
                    type = type,
                    page = page,
                    category = category,
                    location = location,
                    startDate = startDate,
                    endDate = endDate,
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius
                )
                if (storedEventList.isNotEmpty()) {
                    return Pair(
                        storedEventList,
                        true
                    )
                }
                throw e
            } catch (e: Exception) {
                throw e
            }
        }
    }
    suspend fun getCategoryList(): List<CategoryEntity> {
        try {
            return apiDataSource.getCategoryList().toEntity()
        } catch (e: Exception) {
            try {
                val storedCategoryList = localDataSource.getCategoryList()
                if (storedCategoryList.isNotEmpty()) {
                    return storedCategoryList
                }
                throw e
            } catch (e: Exception) {
                throw e
            }
        }
    }
    suspend fun getLocationList(): List<LocationEntity> {
        try {
            return apiDataSource.getLocationList().toEntity()
        } catch (e: Exception) {
            try {
                val storedLocationList = localDataSource.getLocationList()
                if (storedLocationList.isNotEmpty()) {
                    return storedLocationList
                }
                throw e
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getEventById(id: Int): EventEntity{
        return try {
            apiDataSource.getEventById(id)
        } catch (e: Exception) {
            try {
                localDataSource.getEventById(id)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}