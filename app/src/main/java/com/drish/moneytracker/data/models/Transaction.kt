package com.drish.moneytracker.data.models

enum class TransactionType {
    I_OWE,
    OWES_ME
}

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}

data class Transaction(
    val id: String = "",
    val userId: String = "",
    val personId: String = "",
    val personName: String = "",
    val amount: Double = 0.0,
    val type: TransactionType = TransactionType.I_OWE,
    val description: String = "",
    val category: String = "",
    val priority: Priority = Priority.MEDIUM,
    val date: Long = System.currentTimeMillis(),
    val groupId: String = "",
    val isPaid: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
