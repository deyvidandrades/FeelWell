package com.deyvidandrades.feelwell.ui.screens.mood

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.ui.screens.main.pages.CardPosition
import com.deyvidandrades.feelwell.ui.screens.settings.NotificationPermissionRequester


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMoodScreen(newMoodViewModel: NewMoodViewModel, onBackPressed: () -> Unit) {
    val userName by newMoodViewModel.stateFlowUserName.collectAsStateWithLifecycle()

    var moodSelected by remember { mutableStateOf(Mood.MOODTYPE.NONE) }
    var moodReasonArray by remember { mutableStateOf(ArrayList<Mood.REASON>()) }

    BackHandler(enabled = true) { onBackPressed.invoke() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(stringResource(R.string.como_foi_seu_dia))
                        Text("${userName}?", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed.invoke() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LazyVerticalGrid(
                    GridCells.Fixed(3),
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val listMoodTypes = Mood.MOODTYPE.entries.filter { it != Mood.MOODTYPE.NONE }
                    itemsIndexed(listMoodTypes) { index, moodType ->
                        val shape = when (index) {
                            0 -> CardPosition.TOP_LEFT
                            2 -> CardPosition.TOP_RIGHT
                            listMoodTypes.count() - 3 -> CardPosition.BOTTOM_LEFT
                            listMoodTypes.count() - 1 -> CardPosition.BOTTOM_RIGHT
                            else -> CardPosition.MIDDLE
                        }

                        ItemMoodType(
                            moodType,
                            shape,
                            moodSelected == moodType,
                            Modifier.weight(1f),
                            onSelected = { moodSelected = moodType }
                        )
                    }
                }

                if (moodSelected != Mood.MOODTYPE.NONE) {
                    Card(
                        Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(stringResource(R.string.qual_foi_o_motivo), style = MaterialTheme.typography.titleLarge)

                            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Mood.REASON.entries.sortedBy { it.name.count() }.forEach { reason ->
                                    if (reason != Mood.REASON.NONE) {
                                        var selected by remember { mutableStateOf(false) }
                                        FilterChip(
                                            selected,
                                            onClick = {
                                                selected = !selected
                                                if (selected) moodReasonArray.add(reason) else moodReasonArray.remove(
                                                    reason
                                                )
                                            },
                                            label = { Text(reason.name.lowercase()) },
                                            leadingIcon = { if (selected) Icon(Icons.Rounded.Check, null) }
                                        )
                                    }
                                }
                            }

                            ExtendedFloatingActionButton(
                                onClick = {
                                    newMoodViewModel.addNewMood(moodSelected, moodReasonArray)
                                    onBackPressed.invoke()
                                },
                                icon = { Icon(Icons.Rounded.Add, null) },
                                text = { Text(stringResource(R.string.salvar)) },
                                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemMoodType(
    moodType: Mood.MOODTYPE,
    shapePosition: CardPosition,
    isSelected: Boolean,
    modifier: Modifier,
    onSelected: () -> Unit
) {
    val cardShape = when (shapePosition) {
        CardPosition.TOP_LEFT ->
            RoundedCornerShape(topStart = 24.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp)

        CardPosition.TOP_RIGHT ->
            RoundedCornerShape(topStart = 8.dp, topEnd = 24.dp, bottomStart = 8.dp, bottomEnd = 8.dp)

        CardPosition.BOTTOM_LEFT ->
            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 24.dp, bottomEnd = 8.dp)

        CardPosition.BOTTOM_RIGHT ->
            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 24.dp)

        else -> RoundedCornerShape(8.dp)
    }

    Card(
        modifier
            .clip(cardShape)
            .clickable(true, onClick = { onSelected.invoke() }),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        shape = cardShape, border = if (isSelected)
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(moodType.emoji, style = MaterialTheme.typography.displaySmall)
                    Text(
                        moodType.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleSmall,
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
}