package com.example.rightplace.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightplace.database.model.Space

@Dao
interface SpaceDao {
    @Query("SELECT * FROM space")
    suspend fun getAll(): List<Space>

    @Insert
    fun insertAll(space: Space)

    @Delete
    fun delete(space: Space)
}