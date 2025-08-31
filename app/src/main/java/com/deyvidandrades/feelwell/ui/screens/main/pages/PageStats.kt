package com.deyvidandrades.feelwell.ui.screens.main.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.QuickActionAverage

enum class CardPosition { LEFT, MIDDLE, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

@Composable
fun PageStats(stateFlowPositiveEmotions: Float, stateFlowMoodAverages: ArrayList<QuickActionAverage>) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Card(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.emocoes_positivas), style = MaterialTheme.typography.titleLarge)

                LinearProgressIndicator(
                    progress = { stateFlowPositiveEmotions / 100f },
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${stateFlowPositiveEmotions.toInt()}%", style = MaterialTheme.typography.titleSmall)
                    Text("${100 - stateFlowPositiveEmotions.toInt()}%", style = MaterialTheme.typography.titleSmall)
                }
            }
        }

        LazyVerticalGrid(
            GridCells.Fixed(3),
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(stateFlowMoodAverages.sortedByDescending { it.value }) { index, moodAverage ->
                val shape = when (index) {
                    0 -> CardPosition.TOP_LEFT
                    2 -> CardPosition.TOP_RIGHT
                    stateFlowMoodAverages.count() - 3 -> CardPosition.BOTTOM_LEFT
                    stateFlowMoodAverages.count() - 1 -> CardPosition.BOTTOM_RIGHT
                    else -> CardPosition.MIDDLE
                }

                ItemStatsCardMood(
                    moodAverage,
                    shape,
                    Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ItemStatsCardMood(quickActionAverage: QuickActionAverage, shapePosition: CardPosition, modifier: Modifier) {
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
                    stringResource(quickActionAverage.mood.title),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}