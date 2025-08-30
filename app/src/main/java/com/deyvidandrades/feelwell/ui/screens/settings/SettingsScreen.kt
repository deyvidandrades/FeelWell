package com.deyvidandrades.feelwell.ui.screens.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.ui.dialogs.ConfirmationDialog
import com.deyvidandrades.feelwell.ui.dialogs.QuickActionDialog
import com.deyvidandrades.feelwell.ui.dialogs.UserNameDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsScreenViewModel: SettingsScreenViewModel, onBackPressed: () -> Unit) {
    val settings by settingsScreenViewModel.stateFlowSettings.collectAsStateWithLifecycle()

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showDialogQuickAction1 by remember { mutableStateOf(false) }
    var showDialogQuickAction2 by remember { mutableStateOf(false) }
    var showDialogQuickAction3 by remember { mutableStateOf(false) }

    var showUserNameDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val localUriHandler = LocalUriHandler.current

    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName

    // Back to home on back press
    BackHandler(enabled = true) { onBackPressed.invoke() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.configuracoes)) },
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
            LazyColumn {
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {

                        SectionCustomization(
                            settings.isDarkTheme,
                            settings.notifications,
                            settings.notificationTime,
                            onThemeChanged = { settingsScreenViewModel.setDarkTheme(it) },
                            onNotificationChanged = { settingsScreenViewModel.setNotifications(it, context) },
                            onNotificationTimeChanged = { settingsScreenViewModel.setNotificationTime(it, context) }
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(stringResource(R.string.acoes_rapidas), style = MaterialTheme.typography.titleLarge)

                            ItemButton(
                                "${settings.quickAction1.emoji} ${
                                    settings.quickAction1.name.lowercase().replaceFirstChar { it.uppercase() }
                                }",
                                RoundedCornerShape(
                                    topStart = 24.dp,
                                    topEnd = 24.dp,
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp
                                ),
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                onClicked = { showDialogQuickAction1 = true }
                            )
                            ItemButton(
                                "${settings.quickAction2.emoji} ${
                                    settings.quickAction2.name.lowercase().replaceFirstChar { it.uppercase() }
                                }",
                                RoundedCornerShape(8.dp),
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                onClicked = { showDialogQuickAction2 = true }
                            )
                            ItemButton(
                                "${settings.quickAction3.emoji} ${
                                    settings.quickAction3.name.lowercase().replaceFirstChar { it.uppercase() }
                                }",
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 24.dp,
                                    bottomEnd = 24.dp
                                ),
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                onClicked = { showDialogQuickAction3 = true }
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(stringResource(R.string.seus_dados), style = MaterialTheme.typography.titleLarge)

                            ItemButton(
                                stringResource(R.string.nome_de_usuario),
                                RoundedCornerShape(
                                    topStart = 24.dp,
                                    topEnd = 24.dp,
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp
                                ),
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                onClicked = { showUserNameDialog = true }
                            )

                            ItemButton(
                                stringResource(R.string.deletar_dados),
                                RoundedCornerShape(8.dp),
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                onClicked = { showConfirmationDialog = true }
                            )

                            ItemButton(
                                stringResource(R.string.termos_e_condicoes),
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 24.dp,
                                    bottomEnd = 24.dp
                                ),
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                onClicked = {
                                    localUriHandler.openUri(context.getString(R.string.termos_url))
                                }
                            )
                        }

                        Text(
                            "${stringResource(R.string.app_name)} v$versionName",
                            Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            if (showConfirmationDialog)
                ConfirmationDialog(
                    Icons.Rounded.DeleteForever,
                    stringResource(R.string.deletando_os_registros),
                    stringResource(R.string.deseja_deletar_todos_os_registros),
                    onConfirm = {
                        settingsScreenViewModel.deleteAllMoodData()
                        showConfirmationDialog = false
                    },
                    onDismiss = { showConfirmationDialog = false }
                )

            if (showUserNameDialog) UserNameDialog( settings.userName,
                onUserNameSet = {
                    settingsScreenViewModel.setUserName(it)
                    showUserNameDialog = false
                },
                onDismiss = { showUserNameDialog = false }
            )

            if (showDialogQuickAction1) QuickActionDialog(
                onActionSelected = { settingsScreenViewModel.setQuickAction1(it) },
                onDismiss = { showDialogQuickAction1 = false }
            )

            if (showDialogQuickAction2) QuickActionDialog(
                onActionSelected = { settingsScreenViewModel.setQuickAction2(it) },
                onDismiss = { showDialogQuickAction2 = false }
            )

            if (showDialogQuickAction3) QuickActionDialog(
                onActionSelected = { settingsScreenViewModel.setQuickAction3(it) },
                onDismiss = { showDialogQuickAction3 = false }
            )
        }
    }
}

@Composable
private fun SectionCustomization(
    darkTheme: Boolean,
    notifications: Boolean,
    notificationTime: Int,
    onThemeChanged: (Boolean) -> Unit,
    onNotificationChanged: (Boolean) -> Unit,
    onNotificationTimeChanged: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Customization", style = MaterialTheme.typography.titleLarge)

        ItemSwitch(
            stringResource(R.string.tema_escuro),
            darkTheme,
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = 8.dp,
                bottomEnd = 8.dp
            ),
            onCheckedChanged = { onThemeChanged.invoke(it) })

        ItemSwitch(
            stringResource(R.string.notificacoes),
            notifications,
            RoundedCornerShape(8.dp),
            onCheckedChanged = { onNotificationChanged.invoke(it) })

        ItemNumberSwitch(
            stringResource(R.string.horario),
            notificationTime,
            RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            ),
            onSwitchChanged = { onNotificationTimeChanged.invoke(it) })
    }
}

@Composable
private fun ItemSwitch(
    title: String,
    checked: Boolean,
    shape: RoundedCornerShape,
    onCheckedChanged: (Boolean) -> Unit
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title)
            Switch(checked, onCheckedChange = { onCheckedChanged.invoke(it) })
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun ItemNumberSwitch(title: String, number: Int, shape: RoundedCornerShape, onSwitchChanged: (Int) -> Unit) {
    Card(
        Modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 6.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title)

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    val newNumber = if (number - 1 > -1) number - 1 else 23
                    onSwitchChanged.invoke(newNumber)
                }) {
                    Icon(
                        Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text("${if (number > 9) number else "0$number"}:00", fontWeight = FontWeight.Black)

                IconButton(onClick = {
                    val newNumber = if (number + 1 < 24) number + 1 else 0
                    onSwitchChanged.invoke(newNumber)
                }) {
                    Icon(
                        Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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