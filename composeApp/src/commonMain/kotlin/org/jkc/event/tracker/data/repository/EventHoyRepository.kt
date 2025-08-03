package org.jkc.event.tracker.data.repository

import org.jkc.event.tracker.data.datasource.api.IAPIDataSource
import org.jkc.event.tracker.data.datasource.local.ILocalDataSource
import org.jkc.event.tracker.data.entity.response.toEntity
import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity

class EventHoyRepository(
    private val apiDataSource: IAPIDataSource,
    private val localDataSource: ILocalDataSource
) {
    suspend fun getEventList(
        text: String? = null,
        type: String? = null,
        page: Int? = null
    ): Pair<List<EventEntity>, Boolean> {
        try {
            val data = apiDataSource.getEventList(
                text = text,
                type = type,
                page = page
            )
            return Pair(
                data.events.toEntity(),
                data.totalPages != data.currentPage
            )
        } catch (e: Exception) {
            try {
                val storedEventList = localDataSource.getEventList(
                    text = text,
                    type = type,
                    page = page
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
            return apiDataSource.getCategoryList().data.toEntity()
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