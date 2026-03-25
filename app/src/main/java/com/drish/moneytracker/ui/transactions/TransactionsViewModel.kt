package com.drish.moneytracker.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drish.moneytracker.data.models.Person
import com.drish.moneytracker.data.models.Transaction
import com.drish.moneytracker.data.models.TransactionType
import com.drish.moneytracker.data.repository.AuthRepository
import com.drish.moneytracker.data.repository.PersonRepository
import com.drish.moneytracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionsUiState(
    val persons: List<Person> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val activeFilter: TransactionType? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val userId = authRepository.currentUser?.id ?: return
        viewModelScope.launch {
            personRepository.getPersons(userId).collect { persons ->
                _uiState.value = _uiState.value.copy(persons = persons, isLoading = false)
            }
        }
        viewModelScope.launch {
            transactionRepository.getTransactions(userId).collect { transactions ->
                _uiState.value = _uiState.value.copy(transactions = transactions)
            }
        }
    }

    fun setFilter(type: TransactionType?) {
        _uiState.value = _uiState.value.copy(activeFilter = type)
    }

    fun getTransactionsForPerson(personId: String): List<Transaction> {
        val filter = _uiState.value.activeFilter
        return _uiState.value.transactions.filter { tx ->
            tx.personId == personId && (filter == null || tx.type == filter)
        }
    }
}
