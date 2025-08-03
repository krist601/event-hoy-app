package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class DeleteEventResponse(
    val message: String
)