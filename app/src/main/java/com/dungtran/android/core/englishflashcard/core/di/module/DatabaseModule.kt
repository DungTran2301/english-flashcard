package com.dungtran.android.core.englishflashcard.core.di.module

import android.app.Application
import androidx.room.Room
import com.dungtran.android.core.englishflashcard.core.data.db.dao.CardDao
import com.dungtran.android.core.englishflashcard.core.data.db.dao.SetDao
import com.dungtran.android.core.englishflashcard.core.data.db.database.AppDatabase
import com.dungtran.android.core.englishflashcard.core.data.db.database.AppDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModuleDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideCardDao(appDatabase: AppDatabase): CardDao {
        return appDatabase.cardDAO()
    }

    @Provides
    fun provideSetDao(appDatabase: AppDatabase): SetDao {
        return appDatabase.setDAO()
    }
}