package com.drish.moneytracker.data.repository

import com.drish.moneytracker.data.local.dao.PersonDao
import com.drish.moneytracker.data.local.entities.PersonEntity
import com.drish.moneytracker.data.models.Person
import com.drish.moneytracker.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val personDao: PersonDao,
    private val firestoreService: FirestoreService
) {

    fun getPersons(userId: String): Flow<List<Person>> {
        return personDao.getPersonsByUser(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    fun searchPersons(userId: String, query: String): Flow<List<Person>> {
        return personDao.searchPersons(userId, query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getPersonById(personId: String): Person? {
        return personDao.getPersonById(personId)?.toDomainModel()
    }

    suspend fun savePerson(person: Person): Person {
        val personWithId = if (person.id.isEmpty()) {
            person.copy(id = UUID.randomUUID().toString())
        } else person
        personDao.insertPerson(personWithId.toEntity())
        try {
            firestoreService.savePerson(personWithId.userId, personWithId)
        } catch (e: Exception) {
            // Offline-first: local save succeeded, remote sync will happen later
        }
        return personWithId
    }

    suspend fun deletePerson(person: Person) {
        personDao.deletePerson(person.toEntity())
        try {
            firestoreService.deletePerson(person.userId, person.id)
        } catch (e: Exception) {
            // Offline-first: local delete succeeded, remote sync will happen later
        }
    }

    private fun PersonEntity.toDomainModel() = Person(
        id = id,
        userId = userId,
        name = name,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        totalOwedToMe = totalOwedToMe,
        totalIOwe = totalIOwe,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Person.toEntity() = PersonEntity(
        id = id,
        userId = userId,
        name = name,
        phoneNumber = phoneNumber,
        photoUrl = photoUrl,
        totalOwedToMe = totalOwedToMe,
        totalIOwe = totalIOwe,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
