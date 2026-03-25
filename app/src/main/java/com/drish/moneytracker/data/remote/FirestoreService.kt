package com.drish.moneytracker.data.remote

import com.drish.moneytracker.data.models.Group
import com.drish.moneytracker.data.models.GroupMember
import com.drish.moneytracker.data.models.Person
import com.drish.moneytracker.data.models.Transaction
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private fun usersCollection() = firestore.collection("users")
    private fun personsCollection(userId: String) =
        usersCollection().document(userId).collection("persons")
    private fun transactionsCollection(userId: String) =
        usersCollection().document(userId).collection("transactions")
    private fun groupsCollection() = firestore.collection("groups")

    // Person operations
    fun getPersons(userId: String): Flow<List<Person>> = callbackFlow {
        val listener = personsCollection(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val persons = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Person::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(persons)
            }
        awaitClose { listener.remove() }
    }

    suspend fun savePerson(userId: String, person: Person) {
        personsCollection(userId).document(person.id).set(person).await()
    }

    suspend fun deletePerson(userId: String, personId: String) {
        personsCollection(userId).document(personId).delete().await()
    }

    // Transaction operations
    fun getTransactions(userId: String): Flow<List<Transaction>> = callbackFlow {
        val listener = transactionsCollection(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val transactions = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Transaction::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(transactions)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveTransaction(userId: String, transaction: Transaction) {
        transactionsCollection(userId).document(transaction.id).set(transaction).await()
    }

    suspend fun deleteTransaction(userId: String, transactionId: String) {
        transactionsCollection(userId).document(transactionId).delete().await()
    }

    // Group operations
    fun getGroups(userId: String): Flow<List<Group>> = callbackFlow {
        val listener = groupsCollection()
            .whereArrayContains("memberIds", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val groups = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Group::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(groups)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveGroup(group: Group) {
        groupsCollection().document(group.id).set(group).await()
    }

    suspend fun deleteGroup(groupId: String) {
        groupsCollection().document(groupId).delete().await()
    }

    suspend fun addGroupMember(groupId: String, member: GroupMember) {
        groupsCollection().document(groupId)
            .collection("members")
            .document(member.userId)
            .set(member)
            .await()
    }
}
