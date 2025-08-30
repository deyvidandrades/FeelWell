package com.deyvidandrades.feelwell.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.Mood


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDisplayMood(mood: Mood, onDismiss: () -> Unit, onDeleteMood: (Long) -> Unit) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(mood.mood.emoji, style = MaterialTheme.typography.displayMedium)

            Text(
                mood.getDateString().lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.displaySmall
            )

            Text(
                stringResource(R.string.sentindo, mood.mood.name.lowercase().replaceFirstChar { it.uppercase() }),
                style = MaterialTheme.typography.titleMedium
            )

            if (mood.reasons.isNotEmpty())
                FlowRow(
                    Modifier
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mood.reasons.sortedBy { it.name.count() }.forEach { reason ->
                        if (reason != Mood.REASON.NONE) {
                            AssistChip(
                                onClick = { },
                                label = { Text(reason.name.lowercase()) },
                                enabled = false
                            )
                        }
                    }
                }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { showConfirmationDialog = true }) {
                    Text(
                        stringResource(R.string.remover),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Button(onClick = { onDismiss.invoke() }) { Text(stringResource(R.string.fechar)) }
            }
        }
    }

    if (showConfirmationDialog)
        ConfirmationDialog(
            Icons.Rounded.DeleteForever,
            stringResource(R.string.deletando_um_registro),
            stringResource(R.string.voce_deseja_deletar_esse_registro),
            onConfirm = {
                onDeleteMood.invoke(mood.createdAt)
                onDismiss.invoke()
            },
            onDismiss = { showConfirmationDialog = false }
        )
}