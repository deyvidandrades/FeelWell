package com.deyvidandrades.feelwell.ui.screens.main.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowOutward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.model.QuickActionAverage
import com.deyvidandrades.feelwell.ui.dialogs.DialogDisplayMood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageHome(
    stateFlowMoodArray: ArrayList<Mood>,
    stateFlowQuickActionAverages: ArrayList<QuickActionAverage>,
    showNewMood: Boolean,
    onAddNewMood: () -> Unit,
    onDeleteMood: (Long) -> Unit
) {
    var showCardAddMood by remember { mutableStateOf(true) }
    var showDialogMood by remember { mutableStateOf(false) }
    var currentMood: Mood? by remember { mutableStateOf(null) }

    Column(
        Modifier.padding(start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //SearchBox(onSearch = {}, onActionClicked = {})

        Row(
            Modifier
                .fillMaxWidth(),
            //.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ItemStatsCard(stateFlowQuickActionAverages[0], CardPosition.LEFT, Modifier.weight(1f))
            ItemStatsCard(stateFlowQuickActionAverages[1], CardPosition.MIDDLE, Modifier.weight(1f))
            ItemStatsCard(stateFlowQuickActionAverages[2], CardPosition.RIGHT, Modifier.weight(1f))
        }

        if (showCardAddMood && showNewMood)
            CardAddTodayMood(onClick = { onAddNewMood.invoke() })

        if (stateFlowMoodArray.isEmpty()) Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.nada_para_mostrar))
        }

        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            itemsIndexed(stateFlowMoodArray) { index, mood ->

                var shape: RoundedCornerShape

                if (stateFlowMoodArray.count() == 1)
                    shape = RoundedCornerShape(24.dp)
                else if (index == 0)
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                else if (index == stateFlowMoodArray.count() - 1)
                    shape = RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp,
                        topStart = 8.dp,
                        topEnd = 8.dp
                    )
                else
                    shape = RoundedCornerShape(8.dp)

                ItemMood(mood, shape, onClicked = { createdAt ->
                    currentMood = stateFlowMoodArray.find { it.createdAt == createdAt }
                    showDialogMood = true
                })
            }
        }

        if (showDialogMood && currentMood != null)
            DialogDisplayMood(
                currentMood!!,
                onDismiss = { showDialogMood = false },
                onDeleteMood = { onDeleteMood.invoke(it) }
            )
    }
}

@Composable
private fun CardAddTodayMood(onClick: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(true, onClick = { onClick.invoke() }),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
    ) {

        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    stringResource(R.string.como_voce_esta_se_sentindo_hoje),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Icon(Icons.Rounded.ArrowOutward, null)
        }
    }
}

@Composable
private fun ItemMood(mood: Mood, cardShape: Shape, onClicked: (Long) -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(true, onClick = { onClicked.invoke(mood.createdAt) }),
        shape = cardShape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${if (mood.getDayOfMonth() < 10) "0" else ""}${mood.getDayOfMonth()}",
                    style = MaterialTheme.typography.displaySmall
                )
                Text(mood.getMonthString(), style = MaterialTheme.typography.titleSmall)
            }

            VerticalDivider(
                Modifier.height(32.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )

            Text(mood.mood.emoji, style = MaterialTheme.typography.titleLarge)

            Text(
                stringResource(R.string.sentindo, mood.mood.name.lowercase()),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun ItemStatsCard(quickActionAverage: QuickActionAverage, shapePosition: CardPosition, modifier: Modifier) {
    val cardShape = when (shapePosition) {
        CardPosition.LEFT -> RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp, topEnd = 8.dp, bottomEnd = 8.dp)
        CardPosition.RIGHT -> RoundedCornerShape(
            topStart = 8.dp,
            bottomStart = 8.dp,
            topEnd = 24.dp,
            bottomEnd = 24.dp
        )

        else -> RoundedCornerShape(8.dp)
    }

    Card(
        modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = cardShape
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { quickActionAverage.value / 100f },
                    modifier = Modifier.size(72.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    strokeWidth = 12.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                )

                Text(quickActionAverage.mood.emoji, style = MaterialTheme.typography.displaySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${quickActionAverage.value.toInt()}%", style = MaterialTheme.typography.displaySmall)
                Text(
                    quickActionAverage.mood.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleSmall,
                    softWrap = false,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

/*
@Composable
fun UniformCalendar(year: Int, month: Int) {
    // Get the first day of the month to calculate the offset
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1=Mon, 7=Sun
    val daysInMonth = firstDayOfMonth.lengthOfMonth()

    val calendarDays = mutableListOf<Int?>()
    repeat(startDayOfWeek) {
        calendarDays.add(null)
    }
    repeat(daysInMonth) {
        calendarDays.add(it + 1)
    }

    // This is the crucial fix: pad the list with nulls to complete the last week
    while (calendarDays.size % 7 != 0) {
        calendarDays.add(null)
    }

    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Day of the week header
            Row(Modifier.fillMaxWidth()) {
                listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEach { day ->
                    Text(text = day, Modifier.weight(1f), textAlign = TextAlign.Center)
                }
            }

            Spacer(Modifier.height(8.dp))

            // The calendar grid
            calendarDays.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        // DayItem is a Box with a weight of 1f to ensure it takes up an equal share of the row's space
                        Box(
                            Modifier
                                .weight(1f) // This is the key part for uniform sizing
                                .aspectRatio(1f) // Makes the box a square
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (day != null) {
                                FilledIconButton(
                                    onClick = {},
                                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                                    //modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.surfaceContainerHighest)
                                ) { Text(day.toString()) }
                            }
                        }
                    }
                }
            }
        }
    }
}
*/