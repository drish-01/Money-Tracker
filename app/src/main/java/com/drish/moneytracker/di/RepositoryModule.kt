package com.drish.moneytracker.di

import com.drish.moneytracker.data.local.dao.GroupDao
import com.drish.moneytracker.data.local.dao.PersonDao
import com.drish.moneytracker.data.local.dao.TransactionDao
import com.drish.moneytracker.data.remote.FirebaseAuthService
import com.drish.moneytracker.data.remote.FirestoreService
import com.drish.moneytracker.data.repository.AuthRepository
import com.drish.moneytracker.data.repository.GroupRepository
import com.drish.moneytracker.data.repository.PersonRepository
import com.drish.moneytracker.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authService: FirebaseAuthService): AuthRepository {
        return AuthRepository(authService)
    }

    @Provides
    @Singleton
    fun providePersonRepository(
        personDao: PersonDao,
        firestoreService: FirestoreService
    ): PersonRepository {
        return PersonRepository(personDao, firestoreService)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        transactionDao: TransactionDao,
        firestoreService: FirestoreService
    ): TransactionRepository {
        return TransactionRepository(transactionDao, firestoreService)
    }

    @Provides
    @Singleton
    fun provideGroupRepository(
        groupDao: GroupDao,
        firestoreService: FirestoreService
    ): GroupRepository {
        return GroupRepository(groupDao, firestoreService)
    }
}
