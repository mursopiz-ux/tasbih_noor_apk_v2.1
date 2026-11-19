package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dhikrs")
data class DhikrEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val category: String,
    val target: Int,
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val source: String = "",
    val moduleType: String = "GENERAL",
    val sortOrder: Int = 0,
    val sourceType: String = "UMUM"
)
