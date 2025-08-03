package org.jkc.event.tracker.data.datasource.local

import kotlinx.datetime.LocalDateTime
import org.jkc.event.tracker.domain.entity.CategoryEntity
import org.jkc.event.tracker.domain.entity.EventEntity

class LocalDataSource: ILocalDataSource {
    override fun getEventList(
        text: String?,
        type: String?,
        page: Int?
    ): List<EventEntity> {
        return emptyList()
    }

    override fun getEventById(id: Int): EventEntity {
        return EventEntity(
            id = 2,
            title = "Exposición de Arte Contemporáneo",
            description = "Galería abierta con obras de artistas emergentes latinoamericanos.",
            startDate = "2025-08-15T10:00:00Z",
            endDate = "2025-08-30T18:00:00Z",
            image = "https://example.com/images/arte.jpg",
            externalUrl = null,
            source = "Museo de Arte Moderno",
            priceFrom = "0",
            featured = false,
            ticketSaleStart = null,
            ticketSaleEnd = null,
            status = "active",
            categoryId = 5,
            venueId = 8,
            createdAt = "2025-06-10T09:00:00Z",
            updatedAt = "2025-07-05T12:30:00Z"
        )
    }

    override fun getCategoryList(): List<CategoryEntity> {
        return listOf(
            CategoryEntity(
                id = 1,
                name = "Música",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 1,
                status = "published",
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            ),
            CategoryEntity(
                id = 2,
                name = "Deportes",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 2,
                status = "published",
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            ),
            CategoryEntity(
                id = 3,
                name = "Teatro y Comedia",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 3,
                status = "published",
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            ),
            CategoryEntity(
                id = 4,
                name = "Arte y Cultura",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 4,
                status = "published",
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            )
        )
    }
}