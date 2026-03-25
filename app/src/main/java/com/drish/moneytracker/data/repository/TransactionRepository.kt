package com.drish.moneytracker.data.repository

import com.drish.moneytracker.data.local.dao.TransactionDao
import com.drish.moneytracker.data.local.entities.TransactionEntity
import com.drish.moneytracker.data.models.Priority
import com.drish.moneytracker.data.models.Transaction
import com.drish.moneytracker.data.models.TransactionType
import com.drish.moneytracker.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val firestoreService: FirestoreService
) {

    fun getTransactions(userId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByUser(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getTransactionsByPerson(personId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByPerson(personId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getTransactionsByType(userId: String, type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(userId, type.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun getTransactionsByGroup(groupId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByGroup(groupId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getTransactionById(transactionId: String): Transaction? {
        return transactionDao.getTransactionById(transactionId)?.toDomainModel()
    }

    suspend fun saveTransaction(transaction: Transaction): Transaction {
        val txWithId = if (transaction.id.isEmpty()) {
            transaction.copy(id = UUID.randomUUID().toString())
        } else transaction
        transactionDao.insertTransaction(txWithId.toEntity())
        try {
            firestoreService.saveTransaction(txWithId.userId, txWithId)
        } catch (e: Exception) {
            // Offline-first: local save succeeded, remote sync will happen later
        }
        return txWithId
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction.toEntity())
        try {
            firestoreService.deleteTransaction(transaction.userId, transaction.id)
        } catch (e: Exception) {
            // Offline-first: local delete succeeded, remote sync will happen later
        }
    }

    private fun TransactionEntity.toDomainModel() = Transaction(
        id = id,
        userId = userId,
        personId = personId,
        personName = personName,
        amount = amount,
        type = TransactionType.valueOf(type),
        description = description,
        category = category,
        priority = Priority.valueOf(priority),
        date = date,
        groupId = groupId,
        isPaid = isPaid,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Transaction.toEntity() = TransactionEntity(
        id = id,
        userId = userId,
        personId = personId,
        personName = personName,
        amount = amount,
        type = type.name,
        description = description,
        category = category,
        priority = priority.name,
        date = date,
        groupId = groupId,
        isPaid = isPaid,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
