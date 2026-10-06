package com.example.data.repository

import com.example.data.local.dao.ChatMessageDao
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatHistoryRepository(private val dao: ChatMessageDao) {

    fun getChatMessages(userEmail: String): Flow<List<ChatMessage>> {
        return dao.getMessagesForUser(userEmail).map { entities ->
            entities.map { it.toChatMessage() }
        }
    }

    suspend fun saveMessage(message: ChatMessage, userEmail: String) {
        val entity = ChatMessageEntity.fromChatMessage(message, userEmail)
        dao.insertMessage(entity)
    }

    suspend fun saveMessages(messages: List<ChatMessage>, userEmail: String) {
        if (messages.isEmpty()) return
        val entities = messages.map { ChatMessageEntity.fromChatMessage(it, userEmail) }
        dao.insertMessages(entities)
    }

    suspend fun clearHistory(userEmail: String) {
        dao.clearHistoryForUser(userEmail)
    }
}
