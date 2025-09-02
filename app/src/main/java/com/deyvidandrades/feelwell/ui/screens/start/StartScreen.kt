package com.deyvidandrades.feelwell.ui.screens.start

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.NotificationImportant
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.ui.dialogs.QuickActionDialog
import com.deyvidandrades.feelwell.ui.theme.primaryContainerLight

@Composable
fun StartScreen(startScreenViewModel: StartScreenViewModel, onScreenChanged: () -> Unit) {
    val stateFlowIsFirstTime by startScreenViewModel.stateFlowIsFirstTime.collectAsStateWithLifecycle()

    var startIndex by remember { mutableIntStateOf(0) }

    var userName by remember { mutableStateOf("") }
    var notifications by remember { mutableStateOf(false) }
    var quickAction1 by remember { mutableStateOf(Mood.MOODTYPE.FELIZ) }
    var quickAction2 by remember { mutableStateOf(Mood.MOODTYPE.CALMO) }
    var quickAction3 by remember { mutableStateOf(Mood.MOODTYPE.TRISTE) }

    var requestNotification by remember { mutableStateOf(false) }

    LaunchedEffect(stateFlowIsFirstTime) {
        if (!stateFlowIsFirstTime) onScreenChanged.invoke()
    }

    Box(Modifier.fillMaxSize()) {
        Box(Modifier.padding(32.dp)) {
            Icon(
                Icons.Rounded.Spa,
                contentDescription = null,
                tint = primaryContainerLight,
                modifier = Modifier.size(48.dp)
            )
        }

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(painterResource(R.drawable.undraw_welcoming), contentDescription = "", Modifier.height(200.dp))

                Spacer(Modifier.padding(vertical = 24.dp))

                Text(stringResource(R.string.hora_de_acompanhar), style = MaterialTheme.typography.titleLarge)

                Text(
                    stringResource(R.string.suas_emocoes), style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.padding(vertical = 24.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                ) {
                    when (startIndex) {
                        0 -> CardBegin {
                            startIndex += 1
                        }

                        1 -> CardUserName(onBackClicked = { startIndex -= 1 }, onNameSet = {
                            userName = it
                            startIndex += 1
                        })

                        2 -> CardNotificationPermission(
                            onBackClicked = { startIndex -= 1 },
                            onNotificationPermissionRequest = {
                                requestNotification = true
                            }
                        )

                        3 -> CardQuickActions(
                            onBackClicked = { startIndex -= 1 },
                            onNextClicked = { action1, action2, action3 ->
                                quickAction1 = action1
                                quickAction2 = action2
                                quickAction3 = action3
                                startIndex += 1
                            }
                        )

                        4 -> CardReady(
                            userName,
                            onNextClicked = {
                                startScreenViewModel.setStarterInformation(
                                    userName,
                                    notifications,
                                    quickAction1,
                                    quickAction2,
                                    quickAction3
                                )
                                onScreenChanged.invoke()
                            }
                        )

                        else -> startIndex = 0
                    }
                }
            }
        }
    }

    if (requestNotification) NotificationPermissionRequester {
        notifications = it
        startIndex += 1
    }
}

@Composable
private fun CardBegin(onNextClicked: () -> Unit) {
    Column(
        Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(stringResource(R.string.emoji), style = MaterialTheme.typography.titleLarge, fontSize = 48.sp)

        Text(stringResource(R.string.pronto_para_comecar), style = MaterialTheme.typography.titleLarge)

        Text(
            stringResource(R.string.vamos_ajudar_voce_a_configurar_o_app),
            style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center
        )

        ExtendedFloatingActionButton(
            onClick = { onNextClicked.invoke() },
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            icon = { Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null) },
            text = { Text(stringResource(R.string.continuar)) }
        )
    }
}

@Composable
private fun CardUserName(onBackClicked: () -> Unit, onNameSet: (String) -> Unit) {
    var inputText by remember { mutableStateOf("") }
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {

        Text(stringResource(R.string.primeiro_qual_o_seu_nome), style = MaterialTheme.typography.titleLarge)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceDim
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            )

            Text(
                stringResource(R.string.voce_pode_alterar_o_nome_nas_configuracoes),
                style = MaterialTheme.typography.bodySmall
            )

        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { onBackClicked.invoke() }) {
                Text(stringResource(R.string.voltar))
            }

            ExtendedFloatingActionButton(
                onClick = { onNameSet.invoke(inputText) },
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                icon = { Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null) },
                text = { Text(stringResource(R.string.continuar)) }
            )
        }
    }
}

@Composable
private fun CardNotificationPermission(onBackClicked: () -> Unit, onNotificationPermissionRequest: () -> Unit) {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.segundo_notificacoes), style = MaterialTheme.typography.titleLarge)

            Text(
                stringResource(R.string.nosso_app_envia_lembretes_diarios),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            stringResource(R.string.voce_pode_desativar_as_notificacoes_a_qualquer_momento),
            style = MaterialTheme.typography.bodySmall
        )

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { onBackClicked.invoke() }) {
                Text(stringResource(R.string.voltar))
            }

            ExtendedFloatingActionButton(
                onClick = { onNotificationPermissionRequest.invoke() },
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                icon = { Icon(Icons.Rounded.NotificationImportant, contentDescription = null) },
                text = { Text(stringResource(R.string.permitir)) }
            )
        }
    }
}

@Composable
private fun CardQuickActions(onBackClicked: () -> Unit, onNextClicked: (Mood.MOODTYPE, Mood.MOODTYPE, Mood.MOODTYPE) -> Unit) {
    var showQuickActionDialog1 by remember { mutableStateOf(false) }
    var showQuickActionDialog2 by remember { mutableStateOf(false) }
    var showQuickActionDialog3 by remember { mutableStateOf(false) }

    var quickAction1 by remember { mutableStateOf(Mood.MOODTYPE.FELIZ) }
    var quickAction2 by remember { mutableStateOf(Mood.MOODTYPE.CALMO) }
    var quickAction3 by remember { mutableStateOf(Mood.MOODTYPE.TRISTE) }

    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.terceiro_quick_actions), style = MaterialTheme.typography.titleLarge)

        Text(stringResource(R.string.configure_acoes_rapidas_para_suas_notificacoes), style = MaterialTheme.typography.bodyLarge)

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            ItemQuickAction(
                quickAction1,
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 8.dp, bottomEnd = 8.dp),
                onClicked = { showQuickActionDialog1 = true }
            )
            ItemQuickAction(
                quickAction2,
                RoundedCornerShape(8.dp),
                onClicked = { showQuickActionDialog2 = true }
            )
            ItemQuickAction(
                quickAction3,
                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
                onClicked = { showQuickActionDialog3 = true }
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { onBackClicked.invoke() }) {
                Text(stringResource(R.string.voltar))
            }

            ExtendedFloatingActionButton(
                onClick = { onNextClicked.invoke(quickAction1, quickAction2, quickAction3) },
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                icon = { Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null) },
                text = { Text(stringResource(R.string.continuar)) }
            )
        }

        if (showQuickActionDialog1) QuickActionDialog(
            onActionSelected = {
                quickAction1 = it
                showQuickActionDialog1 = false
            },
            onDismiss = { showQuickActionDialog1 = false }
        )

        if (showQuickActionDialog2) QuickActionDialog(
            onActionSelected = {
                quickAction2 = it
                showQuickActionDialog1 = false
            },
            onDismiss = { showQuickActionDialog2 = false }
        )

        if (showQuickActionDialog3) QuickActionDialog(
            onActionSelected = {
                quickAction3 = it
                showQuickActionDialog1 = false
            },
            onDismiss = { showQuickActionDialog3 = false }
        )
    }
}

@Composable
private fun ItemQuickAction(moodtype: Mood.MOODTYPE, cardShape: Shape, onClicked: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(true, onClick = { onClicked.invoke() }),
        shape = cardShape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(moodtype.emoji, style = MaterialTheme.typography.titleLarge)

            Text(
                stringResource(R.string.sentindo, stringResource(moodtype.title)),
                Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun CardReady(userName: String, onNextClicked: () -> Unit) {
    Column(
        Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.emoji), style = MaterialTheme.typography.titleLarge, fontSize = 48.sp)

        Text(stringResource(R.string.tudo_pronto, userName.split(" ")[0]), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.vamos_comecar), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(Modifier.padding(vertical = 24.dp))

        ExtendedFloatingActionButton(
            onClick = { onNextClicked.invoke() },
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            icon = { Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null) },
            text = { Text(stringResource(R.string.comecar)) }
        )
    }
}

@Composable
private fun NotificationPermissionRequester(onPermissionRequested: (Boolean) -> Unit) {
    val context = LocalContext.current
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onPermissionRequested.invoke(isGranted)
        }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onPermissionRequested.invoke(true)
        }
    }
}