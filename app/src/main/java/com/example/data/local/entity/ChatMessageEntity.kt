package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ChatMessage

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val content: String,
    val isBot: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val userEmail: String = ""
) {
    fun toChatMessage(): ChatMessage {
        return ChatMessage(
            id = id,
            content = content,
            isBot = isBot,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromChatMessage(message: ChatMessage, userEmail: String): ChatMessageEntity {
            return ChatMessageEntity(
                id = message.id,
                content = message.content,
                isBot = message.isBot,
                timestamp = message.timestamp,
                userEmail = userEmail
            )
        }
    }
}
