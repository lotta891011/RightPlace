package com.example.rightplace.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class DocumentType (
    @PrimaryKey val id:String,
    @ColumnInfo(name = "name") val Name:String?,
    @ColumnInfo(name = "description") val Description:String?
)