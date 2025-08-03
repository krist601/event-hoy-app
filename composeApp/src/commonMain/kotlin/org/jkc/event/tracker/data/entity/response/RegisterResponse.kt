package org.jkc.event.tracker.data.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val token: String,
    val user: User
) {
    @Serializable
    data class User(
        val id: Int,
        val email: String,
        val name: String
    )
}