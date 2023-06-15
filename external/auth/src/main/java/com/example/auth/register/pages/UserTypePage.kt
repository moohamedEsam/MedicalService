package com.example.auth.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.app.user.UserType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTypePage(
    userType: UserType?,
    modifier: Modifier = Modifier,
    onUserTypeValueChange: (UserType) -> Unit,
    onCreateAccountClick: () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text(text = "Join as a Donner or Receiver", style = MaterialTheme.typography.headlineMedium)
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { onUserTypeValueChange(UserType.Donner) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Donner", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Donate medicine to the needy")
                }
                RadioButton(
                    selected = userType == UserType.Donner,
                    onClick = { onUserTypeValueChange(UserType.Donner) }
                )
            }
        }

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { onUserTypeValueChange(UserType.Receiver) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Receiver", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Receive medicine from the donors\ncertain conditions must apply to you")
                }
                RadioButton(
                    selected = userType == UserType.Receiver,
                    onClick = { onUserTypeValueChange(UserType.Receiver) }
                )
            }
        }
        Button(
            onClick = onCreateAccountClick,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Account")
        }
    }
}

@Composable
private fun UserTypeDialog(userType: UserType, onDismiss: () -> Unit) {
    val description = when (userType) {
        UserType.Donner -> "Donate medicine to the needy"
        UserType.Receiver -> "Receive medicine from the donors\ncertain conditions must apply to you"
        else -> ""
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(userType.name) },
        text = { Text(description) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PhoneAndUserTypePagePreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        UserTypePage(
            userType = UserType.Donner,
            onUserTypeValueChange = {},
            onCreateAccountClick = {}
        )
    }
}