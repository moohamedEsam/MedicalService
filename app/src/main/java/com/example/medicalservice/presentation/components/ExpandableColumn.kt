package com.example.medicalservice.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExpandableColumn(
    expanded: Boolean = false,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(expanded) }
    Column(
        modifier = Modifier.animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            title()
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null
                )
            }
        }
        if (isExpanded) {
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ExpandableBoxPreview() {
    Surface {
        ExpandableColumn(
            title = { Text(text = "Title", style = MaterialTheme.typography.headlineSmall) },
            content = {
                Column {
                    repeat(10) {
                        Text(text = "Content")
                    }
                }
            },
            expanded = false
        )
    }

}