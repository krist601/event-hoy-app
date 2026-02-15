package org.jkc.event.tracker.data.datasource.local

import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.domain.entity.LocationEntity
import org.jkc.event.tracker.domain.entity.SubCategoryEntity

interface ILocalDataSource {
    fun getEventList(
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
    ): List<EventEntity>
    fun getEventById(id: Int): EventEntity
    fun getCategoryList(): List<CategoryEntity>
    fun getSubCategoryList(categoryId: String?): List<SubCategoryEntity>
    fun getLocationList(): List<LocationEntity>
}