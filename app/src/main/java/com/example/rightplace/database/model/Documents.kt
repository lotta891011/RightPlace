package com.example.rightplace.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey


@Entity (foreignKeys = [
        ForeignKey(
                DocumentTypes::class,
                arrayOf("id"),
        arrayOf("type_id"),

        CASCADE),
        ForeignKey(
                Rooms::class,
                arrayOf("id"),
        arrayOf("room_id"),

        CASCADE)]
)
data class Documents (
        @PrimaryKey val id:Int,
        @ColumnInfo(name = "name") val Name:String?,
        @ColumnInfo(name = "description") val Description:String?,
        @ColumnInfo(name = "type_id") val TypeId:Int,
        @ColumnInfo(name = "room_id") val RoomId:Int,
)