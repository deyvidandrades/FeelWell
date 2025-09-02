package com.deyvidandrades.feelwell.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.deyvidandrades.feelwell.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNameDialog(currentName: String, onUserNameSet: (String) -> Unit, onDismiss: () -> Unit) {
    var input by remember { mutableStateOf(currentName) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.nome_de_usuario), style = MaterialTheme.typography.titleLarge)

            TextField(
                value = input,
                onValueChange = { input = it },
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

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { onDismiss.invoke() }) { Text(stringResource(R.string.voltar)) }
                Button(onClick = { onUserNameSet.invoke(input) }) { Text(stringResource(R.string.salvar)) }
            }
        }
    }
}