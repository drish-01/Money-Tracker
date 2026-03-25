package com.drish.moneytracker.data.models

data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val phoneNumber: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
