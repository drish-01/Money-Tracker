package com.drish.moneytracker.data.repository

import com.drish.moneytracker.data.models.User
import com.drish.moneytracker.data.remote.FirebaseAuthService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authService: FirebaseAuthService
) {

    val currentUser: User? get() = authService.getCurrentUserModel()

    val isLoggedIn: Boolean get() = authService.currentUser != null

    val authStateFlow: Flow<User?> = authService.authStateFlow.map { firebaseUser ->
        firebaseUser?.let {
            User(
                id = it.uid,
                email = it.email ?: "",
                displayName = it.displayName ?: "",
                photoUrl = it.photoUrl?.toString() ?: "",
                phoneNumber = it.phoneNumber ?: ""
            )
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return authService.signIn(email, password).map { firebaseUser ->
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl?.toString() ?: ""
            )
        }
    }

    suspend fun signUp(email: String, password: String, displayName: String): Result<User> {
        return authService.signUp(email, password, displayName).map { firebaseUser ->
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = displayName
            )
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return authService.resetPassword(email)
    }

    fun signOut() {
        authService.signOut()
    }
}
