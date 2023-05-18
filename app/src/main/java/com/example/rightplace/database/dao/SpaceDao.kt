package com.example.rightplace.database.dao

import androidx.room.*
import com.example.rightplace.model.Space
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceDao {
    @Query("SELECT * FROM space")
    fun getAll(): Flow<List<Space>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(space: Space)

    @Delete
    suspend fun delete(space: Space)

    @Update
    suspend fun update(space: Space)
}