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
                city = EventEntity.VenueEntity.CityEntity(
                    id = 10,
                    name = "Santiago",
                    country = EventEntity.VenueEntity.CityEntity.CountryEntity(
                        id = 1,
                        name = "Chile"
                    )
                )
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
                slug = "Música",
                description = "Música",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 1,
                isActive = true,
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            ),
            CategoryEntity(
                id = 2,
                name = "Deportes",
                slug = "Deportes",
                description = "Deportes",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 2,
                isActive = true,
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            ),
            CategoryEntity(
                id = 3,
                name = "Teatro y Comedia",
                slug = "Teatro y Comedia",
                description = "Teatro y Comedia",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 3,
                isActive = true,
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            ),
            CategoryEntity(
                id = 4,
                name = "Arte y Cultura",
                slug = "Arte y Cultura",
                description = "Arte y Cultura",
                icon = "https://applications-media.feverup.com/image/upload/f_auto,w_96,h_96,q_auto:best/fever2/filter/photo/41fd2970-0eb9-11ef-be76-d6e48d311834.png",
                position = 4,
                isActive = true,
                createdAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000),
                updatedAt = LocalDateTime(2025, 7, 30, 1, 4, 30, 858000000)
            )
        )
    }
}