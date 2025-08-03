package org.jkc.event.tracker.data.entity.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)