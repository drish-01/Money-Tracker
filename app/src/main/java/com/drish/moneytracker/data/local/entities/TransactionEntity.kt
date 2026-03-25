package com.drish.moneytracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val personId: String,
    val personName: String,
    val amount: Double,
    val type: String,
    val description: String,
    val category: String,
    val priority: String,
    val date: Long,
    val groupId: String,
    val isPaid: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
