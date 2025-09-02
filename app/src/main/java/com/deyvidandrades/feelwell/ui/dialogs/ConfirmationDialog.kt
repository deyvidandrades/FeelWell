package com.deyvidandrades.feelwell.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.deyvidandrades.feelwell.R

@Composable
fun ConfirmationDialog(icon: ImageVector, title: String, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        icon = { Icon(icon, contentDescription = null) },
        title = { Text(title) },
        text = { Text(text) },
        onDismissRequest = { onDismiss.invoke() },
        confirmButton = {
            TextButton(onClick = { onConfirm.invoke() }) {
                Text(
                    stringResource(R.string.confirmar),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) {
                Text(
                    stringResource(R.string.cancelar),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        iconContentColor = MaterialTheme.colorScheme.secondary,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    )
}