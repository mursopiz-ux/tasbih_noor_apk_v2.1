package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.WiridProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WiridProgressDao {
    @Query("SELECT * FROM wirid_progress WHERE dateKey = :dateKey")
    fun getProgressForDate(dateKey: String): Flow<List<WiridProgressEntity>>

    @Query("SELECT * FROM wirid_progress WHERE id = :id LIMIT 1")
    suspend fun getProgressById(id: String): WiridProgressEntity?

    @Query("SELECT COUNT(*) FROM wirid_progress WHERE dateKey = :dateKey AND isCompleted = 1")
    fun getCompletedCountForDate(dateKey: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: WiridProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<WiridProgressEntity>)

    @Query("DELETE FROM wirid_progress WHERE dateKey = :dateKey AND moduleType = :moduleType")
    suspend fun resetModuleProgressForDate(dateKey: String, moduleType: String)

    @Query("DELETE FROM wirid_progress")
    suspend fun clearAllProgress()

    @Query("SELECT * FROM wirid_progress")
    suspend fun getAllProgressList(): List<WiridProgressEntity>
}
