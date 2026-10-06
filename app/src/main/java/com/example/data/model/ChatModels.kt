package com.example.data.model

data class ChatMessage(
    val id: String,
    val content: String,
    val isBot: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class LoginConfig(
    val email: String = "dummy@etisalat.com",
    val password: String = "",
    val dial: String = "1108596441",
    val deviceId: String = "72e1f59bcb7bda75",
    val model: String = "RMX3939",
    val osVersion: String = "14",
    val platform: String = "Android",
    val billingProfileId: String = "2-PR8Y-1768",
    val accountNumber: String = "1.185136782",
    val token: String = "",
    val chatbotToken: String = ""
)

sealed interface ConnectionStatus {
    object Disconnected : ConnectionStatus
    data class Connecting(val step: String) : ConnectionStatus
    data class Connected(val dial: String, val accountNum: String) : ConnectionStatus
    data class Error(val message: String) : ConnectionStatus
}
