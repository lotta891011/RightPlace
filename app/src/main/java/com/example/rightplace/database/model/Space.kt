package com.example.rightplace.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Space (
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "name") val Name:String?,
    @ColumnInfo(name = "description") val Description:String?,
)