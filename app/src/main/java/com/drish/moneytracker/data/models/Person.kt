package com.drish.moneytracker.data.models

data class Person(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
    val totalOwedToMe: Double = 0.0,
    val totalIOwe: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val netBalance: Double get() = totalOwedToMe - totalIOwe
}
