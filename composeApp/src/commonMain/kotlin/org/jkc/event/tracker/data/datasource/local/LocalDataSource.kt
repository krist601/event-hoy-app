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
            id = 1,
            title = "Concierto de Rock Sinfónico",
            description = "Una experiencia única que mezcla el poder del rock con la majestuosidad de una orquesta sinfónica.",
            status = "active",
            ticketPrice = "25.000 CLP",
            imageUrl = "https://example.com/images/rock-sinfonico.jpg",
            createdAt = LocalDateTime.parse("2025-08-01T12:00:00"),
            updatedAt = LocalDateTime.parse("2025-08-05T14:30:00"),
            venue = EventEntity.VenueEntity(
                id = 101,
                name = "Teatro Municipal de Santiago",
                address = "Agustinas 794",
                latitude = -30.01,
                longitude = -30.01,
                url = ""
            ),
            category = EventEntity.CategoryEntity(
                id = 5,
                name = "Música"
            ),
            availableDates = listOf(
                EventEntity.AvailableDatesEntity(
                    id = 1001,
                    startDate = "2025-08-10T20:00:00",
                    endDate = "2025-08-10T22:30:00"
                ),
                EventEntity.AvailableDatesEntity(
                    id = 1002,
                    startDate = "2025-08-11T20:00:00",
                    endDate = "2025-08-11T22:30:00"
                )
            ),
            totalDates = 2,
            nextDate = "2025-08-10T20:00:00",
            recurrenceInfo = EventEntity.RecurrenceInfoEntity(
                recurrenceType = "daily",
                interval = 1,
                startDate = "2025-08-10T20:00:00",
                endDate = "2025-08-11T22:30:00"
            )
        )
    }

    override fun getCategoryList(): List<CategoryEntity> {
        return listOf(
            CategoryEntity(
                id = 1,
                name = "Música",
                description = "Música",
                imageUrl = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                order = 1
            ),
            CategoryEntity(
                id = 2,
                name = "Deportes",
                description = "Deportes",
                imageUrl = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                order = 2,
            ),
            CategoryEntity(
                id = 3,
                name = "Teatro y Comedia",
                description = "Teatro y Comedia",
                imageUrl = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                order = 3,
            ),
            CategoryEntity(
                id = 4,
                name = "Arte y Cultura",
                description = "Arte y Cultura",
                imageUrl = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                order = 4,
            )
        )
    }
}