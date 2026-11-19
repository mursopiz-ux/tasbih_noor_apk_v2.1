package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DhikrEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DhikrDao {
    @Query("SELECT * FROM dhikrs ORDER BY isFavorite DESC, sortOrder ASC, createdAt ASC")
    fun getAllDhikrs(): Flow<List<DhikrEntity>>

    @Query("SELECT * FROM dhikrs WHERE moduleType = :moduleType ORDER BY sortOrder ASC, createdAt ASC")
    fun getDhikrsByModule(moduleType: String): Flow<List<DhikrEntity>>

    @Query("SELECT * FROM dhikrs WHERE id = :id LIMIT 1")
    suspend fun getDhikrById(id: String): DhikrEntity?

    @Query("SELECT * FROM dhikrs WHERE isFavorite = 1 ORDER BY sortOrder ASC, createdAt ASC")
    fun getFavoriteDhikrs(): Flow<List<DhikrEntity>>

    @Query("SELECT * FROM dhikrs WHERE isCustom = 1 ORDER BY createdAt DESC")
    fun getCustomDhikrs(): Flow<List<DhikrEntity>>

    @Query("SELECT * FROM dhikrs WHERE name LIKE '%' || :query || '%' OR transliteration LIKE '%' || :query || '%' OR translation LIKE '%' || :query || '%' OR arabic LIKE '%' || :query || '%'")
    fun searchDhikrs(query: String): Flow<List<DhikrEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDhikrs(dhikrs: List<DhikrEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDhikrsIgnore(dhikrs: List<DhikrEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDhikr(dhikr: DhikrEntity)

    @Update
    suspend fun updateDhikr(dhikr: DhikrEntity)

    @Delete
    suspend fun deleteDhikr(dhikr: DhikrEntity)

    @Query("UPDATE dhikrs SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE dhikrs SET target = :target WHERE id = :id")
    suspend fun updateTarget(id: String, target: Int)

    @Query("DELETE FROM dhikrs WHERE isCustom = 1")
    suspend fun deleteAllCustomDhikrs()

    @Query("SELECT COUNT(*) FROM dhikrs")
    suspend fun getCount(): Int

    @Query("SELECT * FROM dhikrs ORDER BY sortOrder ASC, createdAt ASC")
    suspend fun getAllDhikrsList(): List<DhikrEntity>
}
