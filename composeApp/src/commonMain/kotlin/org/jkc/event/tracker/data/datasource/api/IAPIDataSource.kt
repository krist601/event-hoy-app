package org.jkc.event.tracker.data.datasource.api

import org.jkc.event.tracker.data.entity.response.CategoryResponse
import org.jkc.event.tracker.data.entity.response.EventListResponse
import org.jkc.event.tracker.data.entity.response.LocationResponse
import org.jkc.event.tracker.data.entity.response.SubCategoryResponse
import org.jkc.event.tracker.domain.entity.EventEntity

interface IAPIDataSource {
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
        radius: Int? = null,
    ): EventListResponse
    suspend fun getEventById(id: Int): EventEntity
    suspend fun getCategoryList(): List<CategoryResponse>
    suspend fun getLocationList(): List<LocationResponse>
    suspend fun getSubCategoryList(categoryId: String?): List<SubCategoryResponse>
}