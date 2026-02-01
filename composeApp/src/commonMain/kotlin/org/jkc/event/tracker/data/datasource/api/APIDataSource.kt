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
        date: String?,
        category: String?
    ): EventListResponse {
        val params = mutableListOf<String>()

        text?.let { params += "title=$it" }
        category?.let { params += "categoryId=$it" }
        //type?.let { params += "type=$it" }
        //date?.let{ params += "startdate=$it" }
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

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/api"
    }
}