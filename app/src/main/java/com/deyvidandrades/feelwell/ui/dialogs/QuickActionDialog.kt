package com.deyvidandrades.feelwell.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.Mood


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionDialog(onActionSelected: (Mood.MOODTYPE) -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.acoes_rapidas), style = MaterialTheme.typography.titleLarge)

            LazyColumn(
                Modifier
                    .padding(horizontal = 16.dp)
                    .height(200.dp)
                    .weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(Mood.MOODTYPE.entries.filter { it != Mood.MOODTYPE.NONE }) { index, mood ->
                    val shape = when (index) {
                        0 -> RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        )

                        Mood.MOODTYPE.entries.count() - 1 -> RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        )

                        else -> RoundedCornerShape(8.dp)
                    }

                    ItemButton(
                        "${mood.emoji} ${mood.name.lowercase().replaceFirstChar { it.uppercase() }}",
                        shape,
                        Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        onClicked = {
                            onActionSelected.invoke(mood)
                            onDismiss.invoke()
                        }
                    )
                }
            }

            TextButton(onClick = { onDismiss.invoke() }) { Text(stringResource(R.string.voltar)) }
        }
    }
}

@Composable
private fun ItemButton(title: String, shape: RoundedCornerShape, icon: ImageVector, onClicked: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(true, onClick = { onClicked.invoke() }),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(title, fontWeight = FontWeight.Black)
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}