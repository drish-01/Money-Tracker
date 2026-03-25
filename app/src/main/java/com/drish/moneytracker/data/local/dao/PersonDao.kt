package com.drish.moneytracker.data.local.dao

import androidx.room.*
import com.drish.moneytracker.data.local.entities.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM persons WHERE userId = :userId ORDER BY name ASC")
    fun getPersonsByUser(userId: String): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE id = :personId")
    suspend fun getPersonById(personId: String): PersonEntity?

    @Query("SELECT * FROM persons WHERE userId = :userId AND name LIKE '%' || :query || '%'")
    fun searchPersons(userId: String, query: String): Flow<List<PersonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersons(persons: List<PersonEntity>)

    @Update
    suspend fun updatePerson(person: PersonEntity)

    @Delete
    suspend fun deletePerson(person: PersonEntity)

    @Query("DELETE FROM persons WHERE userId = :userId")
    suspend fun deleteAllPersonsByUser(userId: String)
}
