package org.jkc.event.tracker.data.datasource.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.jkc.event.tracker.data.entity.response.EventListResponse
import org.jkc.event.tracker.data.entity.response.toEntity
import org.jkc.event.tracker.domain.entity.EventEntity
import io.ktor.client.plugins.logging.*
import org.jkc.event.tracker.data.entity.response.CategoryResponse
import org.jkc.event.tracker.data.entity.response.LocationResponse

class APIDataSource: IAPIDataSource {
    private val httpClient = HttpClient {
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    override suspend fun getEventList(
        text: String?,
        type: String?,
        page: Int?,
        category: String?,
        location: String?,
        startDate: String?,
        endDate: String?,
        latitude: Double?,
        longitude: Double?,
        radius: Int?
    ): EventListResponse {
        val params = mutableListOf<String>()

        text?.let { params += "title=$it" }
        category?.let { params += "categoryId=$it" }
        location?.let { params += "venueId=$it" }
        startDate?.let { params += "startDate=$it" }
        endDate?.let { params += "endDate=$it" }
        latitude?.let { params += "latitude=$it" }
        longitude?.let { params += "longitude=$it" }
        radius?.let { params += "radiusKm=$it" }
        //type?.let { params += "type=$it" }
        //page?.let { params += "page=$it" }
        //params += "limit=10"

        val query =
            if(params.isNotEmpty())
                "?" + params.joinToString("&")
            else ""
        return httpClient.get("${BASE_URL}/public/events$query")
            .body<EventListResponse>()
    }

    override suspend fun getEventById(id: Int): EventEntity {
        return httpClient.get("${BASE_URL}/public/events/$id")
            .body<EventListResponse.Event>().toEntity()
    }

    override suspend fun getCategoryList(): List<CategoryResponse> {
        return httpClient.get("${BASE_URL}/public/categories")
            .body<List<CategoryResponse>>()
    }

    override suspend fun getLocationList(): List<LocationResponse> {
        return httpClient.get("${BASE_URL}/public/venues")
            .body<List<LocationResponse>>()
    }

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/api"
    }
}