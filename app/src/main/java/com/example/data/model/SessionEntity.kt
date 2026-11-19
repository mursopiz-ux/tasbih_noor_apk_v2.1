package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey
    val id: String,
    val dhikrId: String,
    val dhikrName: String,
    val arabic: String,
    val count: Int,
    val target: Int,
    val isCompleted: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Long = 0L,
    val moduleType: String = "TASBIH"
)
