package com.drish.moneytracker.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.drish.moneytracker.ui.components.AnimatedBackground
import com.drish.moneytracker.ui.components.GlassCard
import com.drish.moneytracker.ui.components.MoneyTrackerFab
import com.drish.moneytracker.ui.dashboard.components.RecentTransactionItem
import com.drish.moneytracker.ui.dashboard.components.SummaryCard
import com.drish.moneytracker.ui.theme.NegativeRed
import com.drish.moneytracker.ui.theme.PositiveGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AnimatedBackground(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Money Tracker",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                )
            },
            floatingActionButton = { MoneyTrackerFab(onClick = onAddTransaction) },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Net balance card
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Net Balance",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "₹%.2f".format(uiState.netBalance),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.netBalance >= 0) PositiveGreen else NegativeRed
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SummaryCard(
                                modifier = Modifier.weight(1f),
                                title = "Owes Me",
                                amount = uiState.totalOwedToMe,
                                color = PositiveGreen
                            )
                            SummaryCard(
                                modifier = Modifier.weight(1f),
                                title = "I Owe",
                                amount = uiState.totalIOwe,
                                color = NegativeRed
                            )
                        }
                    }

                    if (uiState.recentTransactions.isNotEmpty()) {
                        item {
                            Text(
                                text = "Recent Transactions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        items(uiState.recentTransactions) { transaction ->
                            RecentTransactionItem(transaction = transaction)
                        }
                    }
                }
            }
        }
    }
}
