package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Query("SELECT * FROM chat_messages WHERE userEmail = :userEmail OR userEmail = '' ORDER BY timestamp ASC")
    fun getMessagesForUser(userEmail: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Query("DELETE FROM chat_messages WHERE userEmail = :userEmail OR userEmail = ''")
    suspend fun clearHistoryForUser(userEmail: String)

    @Query("DELETE FROM chat_messages")
    suspend fun clearAll()
}
