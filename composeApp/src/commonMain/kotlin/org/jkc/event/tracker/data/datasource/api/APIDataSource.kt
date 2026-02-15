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
import org.jkc.event.tracker.data.entity.response.SubCategoryResponse

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
        subCategory: String?,
        location: String?,
        startDate: String?,
        endDate: String?,
        latitude: Double?,
        longitude: Double?,
        radius: Int?,
    ): EventListResponse {
        val params = mutableListOf<String>()

        if (!text.isNullOrEmpty()) { params += "title=$text" }
        if (!subCategory.isNullOrEmpty()) { params += "subCategoryId=$subCategory" }
        else if (!category.isNullOrEmpty()) { params += "categoryId=$category" }
        if (!location.isNullOrEmpty()) { params += "venueId=$location" }
        if (!startDate.isNullOrEmpty()) { params += "startDate=${startDate}T00:00:00" }
        if (!endDate.isNullOrEmpty()) { params += "endDate=${endDate}T00:00:00" }
        latitude?.let { params += "latitude=$it" }
        longitude?.let { params += "longitude=$it" }
        //radius?.let { params += "radiusKm=$it" }
        //type?.let { params += "type=$it" }
        page?.let { params += "page=$it" }
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

    override suspend fun getSubCategoryList(categoryId: String?): List<SubCategoryResponse> {
        return httpClient.get("${BASE_URL}/public/subcategories?categoryId=$categoryId")
            .body<List<SubCategoryResponse>>()
    }

    companion object {
        private const val BASE_URL = "https://api.eventhoy.com/api"
    }
}