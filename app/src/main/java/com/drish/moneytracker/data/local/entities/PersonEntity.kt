package com.drish.moneytracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val phoneNumber: String,
    val photoUrl: String,
    val totalOwedToMe: Double,
    val totalIOwe: Double,
    val createdAt: Long,
    val updatedAt: Long
)
