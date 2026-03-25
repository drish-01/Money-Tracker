package com.drish.moneytracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drish.moneytracker.data.local.dao.GroupDao
import com.drish.moneytracker.data.local.dao.PersonDao
import com.drish.moneytracker.data.local.dao.TransactionDao
import com.drish.moneytracker.data.local.entities.GroupEntity
import com.drish.moneytracker.data.local.entities.PersonEntity
import com.drish.moneytracker.data.local.entities.TransactionEntity

@Database(
    entities = [PersonEntity::class, TransactionEntity::class, GroupEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoneyTrackerDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun transactionDao(): TransactionDao
    abstract fun groupDao(): GroupDao
}
