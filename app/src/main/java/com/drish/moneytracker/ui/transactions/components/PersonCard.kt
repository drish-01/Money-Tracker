package com.drish.moneytracker.ui.transactions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.drish.moneytracker.data.models.Person
import com.drish.moneytracker.data.models.Transaction
import com.drish.moneytracker.data.models.TransactionType
import com.drish.moneytracker.ui.components.GlassCard
import com.drish.moneytracker.ui.theme.NegativeRed
import com.drish.moneytracker.ui.theme.PositiveGreen

@Composable
fun PersonCard(
    person: Person,
    transactions: List<Transaction>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (person.phoneNumber.isNotEmpty()) {
                        Text(
                            text = person.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    val netBalance = person.netBalance
                    Text(
                        text = "${if (netBalance >= 0) "+" else ""}₹%.2f".format(netBalance),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (netBalance >= 0) PositiveGreen else NegativeRed
                    )
                    Text(
                        text = if (netBalance >= 0) "Owes You" else "You Owe",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                if (transactions.isNotEmpty()) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    transactions.forEach { transaction ->
                        TransactionItem(transaction = transaction)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description.ifEmpty { transaction.category.ifEmpty { "Transaction" } },
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = transaction.priority.name,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Text(
            text = "${if (transaction.type == TransactionType.OWES_ME) "+" else "-"}₹%.2f".format(transaction.amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (transaction.type == TransactionType.OWES_ME) PositiveGreen else NegativeRed
        )
    }
}
