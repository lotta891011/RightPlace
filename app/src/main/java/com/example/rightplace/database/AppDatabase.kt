package com.example.rightplace.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rightplace.database.dao.DocumentDao
import com.example.rightplace.database.dao.DocumentTypeDao
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentType
import com.example.rightplace.model.Space

@Database(entities = [DocumentType::class, Document::class, Space::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    companion object{
        private var appDatabase: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if(appDatabase != null){
                return appDatabase!!
            }
            appDatabase = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database").build()
            return  appDatabase!!
        }
    }
    abstract fun documentTypeDao(): DocumentTypeDao
    abstract fun documentDao(): DocumentDao
}