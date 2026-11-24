package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.login_button
import com.example.swapcampus_geoffroykevan.resources.login_demo_hint
import com.example.swapcampus_geoffroykevan.resources.login_email
import com.example.swapcampus_geoffroykevan.resources.login_error
import com.example.swapcampus_geoffroykevan.resources.login_password
import com.example.swapcampus_geoffroykevan.resources.login_title
import com.example.swapcampus_geoffroykevan.ui.AppContainer
import com.example.swapcampus_geoffroykevan.ui.viewmodel.AuthViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    viewModel: AuthViewModel = viewModel { AuthViewModel(AppContainer.authRepository) },
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(stringResource(Res.string.login_title), style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text(stringResource(Res.string.login_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text(stringResource(Res.string.login_password)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )
        if (viewModel.loginFailed) {
            Text(stringResource(Res.string.login_error), color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = { if (viewModel.login()) onLoggedIn() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(Res.string.login_button))
        }
        Text(
            stringResource(Res.string.login_demo_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
