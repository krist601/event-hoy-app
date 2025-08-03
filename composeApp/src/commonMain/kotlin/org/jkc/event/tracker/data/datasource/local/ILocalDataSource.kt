package org.jkc.event.tracker.data.datasource.local

import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity

interface ILocalDataSource {
    fun getEventList(
        text: String? = null,
        type: String? = null,
        page: Int? = null
    ): List<EventEntity>
    fun getEventById(id: Int): EventEntity
    fun getCategoryList(): List<CategoryEntity>
}