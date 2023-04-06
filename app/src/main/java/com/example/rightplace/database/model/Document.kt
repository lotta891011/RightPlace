package com.example.rightplace.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey


@Entity (foreignKeys = [
        ForeignKey(
                DocumentType::class,
                arrayOf("id"),
        arrayOf("type_id"),

        CASCADE),
        ForeignKey(
                Space::class,
                arrayOf("id"),
        arrayOf("space_id"),

        CASCADE)]
)
data class Document (
        @PrimaryKey val id:Int,
        @ColumnInfo(name = "name") val Name:String?,
        @ColumnInfo(name = "description") val Description:String?,
        @ColumnInfo(name = "type_id") val TypeId:Int,
        @ColumnInfo(name = "space_id") val RoomId:Int,
        @ColumnInfo(name = "code") val Code:Int
)