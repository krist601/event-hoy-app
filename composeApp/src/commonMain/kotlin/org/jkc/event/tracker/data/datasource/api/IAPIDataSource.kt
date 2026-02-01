package org.jkc.event.tracker.data.datasource.api

import org.jkc.event.tracker.data.entity.response.CategoryResponse
import org.jkc.event.tracker.data.entity.response.EventListResponse
import org.jkc.event.tracker.domain.entity.EventEntity

interface IAPIDataSource {
    suspend fun getEventList(
        text: String? = null,
        type: String? = null,
        page: Int? = null,
        date: String? = null,
        category: String?
    ): EventListResponse
    suspend fun getEventById(id: Int): EventEntity
    suspend fun getCategoryList(): List<CategoryResponse>
}