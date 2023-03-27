package com.example.rightplace.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightplace.database.model.Rooms

@Dao
interface RoomsDao {
    @Query("SELECT * FROM rooms")
    suspend fun getAll(): List<Rooms>

    @Insert
    fun insertAll(rooms: Rooms)

    @Delete
    fun delete(rooms: Rooms)
}