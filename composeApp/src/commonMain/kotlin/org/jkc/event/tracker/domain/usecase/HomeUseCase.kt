package org.jkc.event.tracker.domain.usecase

import org.jkc.event.tracker.data.repository.EventHoyRepository
import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.entity.LocationEntity
import org.jkc.event.tracker.domain.entity.SubCategoryEntity

class HomeUseCase(
    private val eventHoyRepository: EventHoyRepository
) {
    suspend fun getEventList(
        text: String? = null,
        type: String? = null,
        page: Int? = null,
        category: String? = null,
        subCategory: String? = null,
        location: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Int? = null
    ): Pair<List<EventEntity>, Boolean> {
        return eventHoyRepository.getEventList(
            text = text,
            type = type,
            page = page,
            category = category,
            subCategory = subCategory,
            location = location,
            startDate = startDate,
            endDate = endDate,
            latitude = latitude,
            longitude = longitude,
            radius = radius
        )
    }
    suspend fun getEventListType(
        text: String? = null,
        type: String? = null,
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Int? = null
    ): List<EventEntity> {
        return eventHoyRepository.getEventList(
            text = text,
            type = type,
            category = category,
            startDate = startDate,
            endDate = endDate,
            latitude = latitude,
            longitude = longitude,
            radius = radius
        ).first
    }
    suspend fun getCategoryList(): List<CategoryEntity> {
        return eventHoyRepository.getCategoryList()
    }

    suspend fun getLocationList(): List<LocationEntity> {
        return eventHoyRepository.getLocationList()
    }
    suspend fun getSubCategoryList(categoryId: String?): List<SubCategoryEntity> {
        val list = eventHoyRepository.getSubCategoryList(categoryId)
        val allItem = SubCategoryEntity(
            id = -1,
            name = "Todas las categorías",
            categoryId = null,
            order = Int.MIN_VALUE
        )
        return listOf(allItem) + list
    }
}