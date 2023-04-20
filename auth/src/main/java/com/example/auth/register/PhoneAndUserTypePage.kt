package com.example.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.auth.R
import com.example.common.models.ValidationResult
import com.example.einvoicecomponents.textField.ValidationOutlinedTextField
import com.example.models.auth.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PhoneAndUserTypePage(
    phone: StateFlow<String>,
    phoneValidation: StateFlow<ValidationResult>,
    onPhoneValueChange: (String) -> Unit,
    userType: StateFlow<UserType>,
    modifier: Modifier = Modifier,
    onUserTypeValueChange: (UserType) -> Unit,
) {
    var userTypeToShowInfo: UserType? by remember {
        mutableStateOf(null)
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(32.dp)) {
        UserTypeRow(userTypeState = userType, onUserTypeClick = onUserTypeValueChange) {
            userTypeToShowInfo = it
        }
        ValidationOutlinedTextField(
            valueState = phone,
            validationState = phoneValidation,
            label = "Phone",
            onValueChange = onPhoneValueChange
        )

        if (userTypeToShowInfo != null) {
            UserTypeDialog(userType = userTypeToShowInfo!!) {
                userTypeToShowInfo = null
            }
        }

    }
}

@Composable
private fun UserTypeDialog(userType: UserType, onDismiss: () -> Unit) {
    val description = when (userType) {
        UserType.Donner -> "Donate medicine to the needy"
        UserType.Receiver -> "Receive medicine from the donors\ncertain conditions must apply to you"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserTypeRow(
    userTypeState: StateFlow<UserType>,
    onUserTypeClick: (UserType) -> Unit,
    onShowUserTypeInfoClick: (UserType) -> Unit
) {
    val userType by userTypeState.collectAsState()
    Text("User Type", style = MaterialTheme.typography.headlineSmall)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        UserType.values().forEach {
            FilterChip(
                onClick = { onUserTypeClick(it) },
                selected = it == userType,
                label = { Text(it.name) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.question),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onShowUserTypeInfoClick(it) }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneAndUserTypePagePreview() {
    Surface {
        PhoneAndUserTypePage(
            phone = MutableStateFlow(""),
            phoneValidation = MutableStateFlow(ValidationResult.Valid),
            onPhoneValueChange = {},
            userType = MutableStateFlow(UserType.Donner),
            onUserTypeValueChange = {}
        )
    }
}