package com.deyvidandrades.feelwell.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.ui.navigation.SCREENS
import com.deyvidandrades.feelwell.ui.screens.main.pages.PageHome
import com.deyvidandrades.feelwell.ui.screens.main.pages.PageStats
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainScreenViewModel: MainScreenViewModel, onScreenChanged: (SCREENS) -> Unit) {
    val stateFlowUserName by mainScreenViewModel.stateFlowUserName.collectAsStateWithLifecycle()
    val stateFlowMoodArray by mainScreenViewModel.stateFlowMoodArray.collectAsStateWithLifecycle()
    val stateFlowMoodToday by mainScreenViewModel.stateFlowTodayMood.collectAsStateWithLifecycle()

    val stateFlowQuickActionAverages by mainScreenViewModel.stateFlowQuickActionAverages.collectAsStateWithLifecycle()
    val stateFlowPositiveEmotions by mainScreenViewModel.stateFlowPositiveEmotions.collectAsStateWithLifecycle()
    val stateFlowMoodAverages by mainScreenViewModel.stateFlowMoodAverages.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    // Back to home on back press
    BackHandler(enabled = pagerState.currentPage != 0) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(0)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = { TopBar(stateFlowUserName) { onScreenChanged.invoke(SCREENS.SETTINGS) } },
        bottomBar = {
            BottomBar(pagerState.targetPage, onPageChange = {
                coroutineScope.launch { pagerState.animateScrollToPage(it) }
            })
        }
    ) { contentPadding ->
        Box(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                when (page) {
                    0 -> PageHome(
                        stateFlowMoodArray,
                        stateFlowQuickActionAverages,
                        stateFlowMoodToday.mood == Mood.MOODTYPE.NONE,
                        onAddNewMood = { onScreenChanged.invoke(SCREENS.ADD_MOOD) },
                        onDeleteMood = { mainScreenViewModel.removeMood(it) }
                    )

                    1 -> PageStats(stateFlowPositiveEmotions, stateFlowMoodAverages)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(userName: String, onActionClicked: () -> Unit) {
    TopAppBar(
        title = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.ola))
                Text("${userName}!", fontWeight = FontWeight.Bold)
            }
        },
        actions = {
            IconButton(onClick = { onActionClicked.invoke() }) {
                Icon(Icons.Rounded.MoreVert, null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
private fun BottomBar(page: Int, onPageChange: (Int) -> Unit) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        NavigationBarItem(
            selected = page == 0,
            onClick = { onPageChange.invoke(0) },
            icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.home)) }
        )
        NavigationBarItem(
            selected = page == 1,
            onClick = { onPageChange.invoke(1) },
            icon = { Icon(Icons.Rounded.BarChart, contentDescription = null) },
            label = { Text(stringResource(R.string.stats)) }
        )
    }
}

