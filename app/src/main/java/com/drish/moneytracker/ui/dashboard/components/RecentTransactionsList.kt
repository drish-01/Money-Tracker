package com.drish.moneytracker.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drish.moneytracker.data.models.Transaction
import com.drish.moneytracker.data.models.TransactionType
import com.drish.moneytracker.ui.components.GlassCard
import com.drish.moneytracker.ui.theme.NegativeRed
import com.drish.moneytracker.ui.theme.PositiveGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentTransactionItem(transaction: Transaction) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.personName,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = transaction.description.ifEmpty { transaction.category },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                        .format(Date(transaction.date)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.type == TransactionType.OWES_ME) "+" else "-"}₹%.2f".format(transaction.amount),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (transaction.type == TransactionType.OWES_ME) PositiveGreen else NegativeRed
                )
                Text(
                    text = if (transaction.type == TransactionType.OWES_ME) "Owes Me" else "I Owe",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
