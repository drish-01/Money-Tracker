package com.drish.moneytracker.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drish.moneytracker.data.models.Group
import com.drish.moneytracker.data.repository.AuthRepository
import com.drish.moneytracker.data.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupsUiState(
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupsUiState())
    val uiState: StateFlow<GroupsUiState> = _uiState.asStateFlow()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        val userId = authRepository.currentUser?.id ?: ""
        viewModelScope.launch {
            groupRepository.getGroups(userId).collect { groups ->
                _uiState.value = _uiState.value.copy(groups = groups, isLoading = false)
            }
        }
    }

    fun createGroup(name: String, description: String) {
        val userId = authRepository.currentUser?.id ?: ""
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val group = Group(
                    name = name,
                    description = description,
                    createdBy = userId
                )
                groupRepository.saveGroup(group)
                _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to create group"
                )
            }
        }
    }

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            try {
                groupRepository.deleteGroup(group)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }
}
