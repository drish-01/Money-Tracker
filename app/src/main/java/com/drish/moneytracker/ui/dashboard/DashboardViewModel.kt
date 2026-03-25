package com.drish.moneytracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drish.moneytracker.data.models.Transaction
import com.drish.moneytracker.data.models.TransactionType
import com.drish.moneytracker.data.repository.AuthRepository
import com.drish.moneytracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val totalOwedToMe: Double = 0.0,
    val totalIOwe: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true
) {
    val netBalance: Double get() = totalOwedToMe - totalIOwe
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val userId = authRepository.currentUser?.id ?: return
        viewModelScope.launch {
            transactionRepository.getTransactions(userId).collect { transactions ->
                val owedToMe = transactions
                    .filter { it.type == TransactionType.OWES_ME && !it.isPaid }
                    .sumOf { it.amount }
                val iOwe = transactions
                    .filter { it.type == TransactionType.I_OWE && !it.isPaid }
                    .sumOf { it.amount }
                _uiState.value = DashboardUiState(
                    totalOwedToMe = owedToMe,
                    totalIOwe = iOwe,
                    recentTransactions = transactions.take(10),
                    isLoading = false
                )
            }
        }
    }
}
