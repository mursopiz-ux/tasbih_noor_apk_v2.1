package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wirid_progress")
data class WiridProgressEntity(
    @PrimaryKey
    val id: String, // "${dateKey}_${wiridId}"
    val wiridId: String,
    val moduleType: String,
    val dateKey: String, // YYYY-MM-DD
    val currentCount: Int,
    val target: Int,
    val isCompleted: Boolean,
    val sessionRecorded: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
