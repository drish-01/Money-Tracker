package com.drish.moneytracker.di

import android.content.Context
import androidx.room.Room
import com.drish.moneytracker.data.local.dao.GroupDao
import com.drish.moneytracker.data.local.dao.PersonDao
import com.drish.moneytracker.data.local.dao.TransactionDao
import com.drish.moneytracker.data.local.database.MoneyTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoneyTrackerDatabase {
        return Room.databaseBuilder(
            context,
            MoneyTrackerDatabase::class.java,
            "money_tracker_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePersonDao(database: MoneyTrackerDatabase): PersonDao {
        return database.personDao()
    }

    @Provides
    fun provideTransactionDao(database: MoneyTrackerDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideGroupDao(database: MoneyTrackerDatabase): GroupDao {
        return database.groupDao()
    }
}
