package com.drish.moneytracker.ui.groups.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drish.moneytracker.data.models.GroupMember

@Composable
fun MembersList(members: List<GroupMember>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        members.forEach { member ->
            ListItem(
                headlineContent = { Text(member.displayName) },
                supportingContent = { Text(member.email) },
                trailingContent = {
                    Text(
                        text = member.role.name,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
            HorizontalDivider()
        }
    }
}
