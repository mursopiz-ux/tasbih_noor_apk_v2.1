package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getSessionsSince(sinceTimestamp: Long): Flow<List<SessionEntity>>

    @Query("SELECT COALESCE(SUM(count), 0) FROM sessions")
    fun getTotalDhikrCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM sessions WHERE isCompleted = 1")
    fun getTotalCompletedSessions(): Flow<Int>

    @Query("SELECT COALESCE(SUM(count), 0) FROM sessions WHERE timestamp >= :startOfDayTimestamp")
    fun getTodayDhikrCount(startOfDayTimestamp: Long): Flow<Int>

    @Query("SELECT COALESCE(SUM(count), 0) FROM sessions WHERE timestamp >= :sinceTimestamp")
    fun getDhikrCountSince(sinceTimestamp: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessions(sessions: List<SessionEntity>)

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("DELETE FROM sessions")
    suspend fun clearAllSessions()

    @Query("SELECT * FROM sessions ORDER BY timestamp DESC")
    suspend fun getAllSessionsList(): List<SessionEntity>
}
