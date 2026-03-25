package com.drish.moneytracker.ui.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.drish.moneytracker.data.models.TransactionType
import com.drish.moneytracker.ui.components.AnimatedBackground
import com.drish.moneytracker.ui.components.MoneyTrackerFab
import com.drish.moneytracker.ui.transactions.components.PersonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onPersonClick: (String) -> Unit,
    onAddTransaction: () -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AnimatedBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Transactions") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                )
            },
            floatingActionButton = { MoneyTrackerFab(onClick = onAddTransaction) },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                // Filter chips
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.activeFilter == null,
                        onClick = { viewModel.setFilter(null) },
                        label = { Text("All") }
                    )
                    FilterChip(
                        selected = uiState.activeFilter == TransactionType.OWES_ME,
                        onClick = { viewModel.setFilter(TransactionType.OWES_ME) },
                        label = { Text("Owes Me") }
                    )
                    FilterChip(
                        selected = uiState.activeFilter == TransactionType.I_OWE,
                        onClick = { viewModel.setFilter(TransactionType.I_OWE) },
                        label = { Text("I Owe") }
                    )
                }

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                } else if (uiState.persons.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No transactions yet.\nTap + to add one.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.persons) { person ->
                            val personTransactions = viewModel.getTransactionsForPerson(person.id)
                            if (personTransactions.isNotEmpty() || uiState.activeFilter == null) {
                                PersonCard(
                                    person = person,
                                    transactions = personTransactions,
                                    onClick = { onPersonClick(person.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
