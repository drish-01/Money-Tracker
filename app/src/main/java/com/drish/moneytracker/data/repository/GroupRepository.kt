package com.drish.moneytracker.data.repository

import com.drish.moneytracker.data.local.dao.GroupDao
import com.drish.moneytracker.data.local.entities.GroupEntity
import com.drish.moneytracker.data.models.Group
import com.drish.moneytracker.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(
    private val groupDao: GroupDao,
    private val firestoreService: FirestoreService
) {

    fun getGroups(userId: String): Flow<List<Group>> {
        return groupDao.getAllGroups().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getGroupById(groupId: String): Group? {
        return groupDao.getGroupById(groupId)?.toDomainModel()
    }

    suspend fun saveGroup(group: Group): Group {
        val groupWithId = if (group.id.isEmpty()) {
            group.copy(id = UUID.randomUUID().toString())
        } else group
        groupDao.insertGroup(groupWithId.toEntity())
        try {
            firestoreService.saveGroup(groupWithId)
        } catch (e: Exception) {
            // Offline-first: local save succeeded, remote sync will happen later
        }
        return groupWithId
    }

    suspend fun deleteGroup(group: Group) {
        groupDao.deleteGroup(group.toEntity())
        try {
            firestoreService.deleteGroup(group.id)
        } catch (e: Exception) {
            // Offline-first: local delete succeeded, remote sync will happen later
        }
    }

    private fun GroupEntity.toDomainModel() = Group(
        id = id,
        name = name,
        description = description,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Group.toEntity() = GroupEntity(
        id = id,
        name = name,
        description = description,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
