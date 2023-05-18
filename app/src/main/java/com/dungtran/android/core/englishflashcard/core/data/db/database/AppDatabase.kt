package com.dungtran.android.core.englishflashcard.core.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dungtran.android.core.englishflashcard.core.data.db.dao.CardDao
import com.dungtran.android.core.englishflashcard.core.data.db.dao.SetDao
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB

@Database(
    entities = [CardDB::class, SetDB::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardDAO(): CardDao
    abstract fun setDAO(): SetDao

    companion object {
        const val DATABASE_NAME = "APP_DB"

//        @Volatile
//        private var instance: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            return instance ?: synchronized(this) {
//                instance ?: buildDatabase(context).also { instance = it }
//            }
//        }
//
//        private fun buildDatabase(context: Context): AppDatabase {
//            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
//                .build()
//        }

    }

}