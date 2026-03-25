package com.drish.moneytracker.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drish.moneytracker.data.models.Person
import com.drish.moneytracker.data.models.Priority
import com.drish.moneytracker.data.models.Transaction
import com.drish.moneytracker.data.models.TransactionType
import com.drish.moneytracker.data.repository.AuthRepository
import com.drish.moneytracker.data.repository.PersonRepository
import com.drish.moneytracker.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTransactionUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val personRepository: PersonRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    fun saveTransaction(
        personName: String,
        amount: Double,
        type: TransactionType,
        description: String,
        category: String,
        priority: Priority
    ) {
        val userId = authRepository.currentUser?.id ?: run {
            _uiState.value = AddTransactionUiState(errorMessage = "User not logged in")
            return
        }

        viewModelScope.launch {
            _uiState.value = AddTransactionUiState(isLoading = true)
            try {
                // Find or create person
                val persons = personRepository.getPersons(userId).first()

                val existingPerson = persons.find {
                    it.name.equals(personName, ignoreCase = true)
                }
                val person = existingPerson ?: personRepository.savePerson(
                    Person(
                        userId = userId,
                        name = personName
                    )
                )

                // Calculate updated totals
                val updatedPerson = when (type) {
                    TransactionType.OWES_ME -> person.copy(
                        totalOwedToMe = person.totalOwedToMe + amount,
                        updatedAt = System.currentTimeMillis()
                    )
                    TransactionType.I_OWE -> person.copy(
                        totalIOwe = person.totalIOwe + amount,
                        updatedAt = System.currentTimeMillis()
                    )
                }
                personRepository.savePerson(updatedPerson)

                // Save transaction
                val transaction = Transaction(
                    userId = userId,
                    personId = person.id,
                    personName = person.name,
                    amount = amount,
                    type = type,
                    description = description,
                    category = category,
                    priority = priority
                )
                transactionRepository.saveTransaction(transaction)
                _uiState.value = AddTransactionUiState(isSaved = true)
            } catch (e: Exception) {
                _uiState.value = AddTransactionUiState(
                    errorMessage = e.message ?: "Failed to save transaction"
                )
            }
        }
    }
}
