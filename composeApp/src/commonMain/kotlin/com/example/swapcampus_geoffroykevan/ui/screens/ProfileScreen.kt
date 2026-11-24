package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.profile_credits
import com.example.swapcampus_geoffroykevan.resources.profile_email
import com.example.swapcampus_geoffroykevan.resources.profile_logout
import com.example.swapcampus_geoffroykevan.resources.profile_member_since
import com.example.swapcampus_geoffroykevan.resources.profile_name
import com.example.swapcampus_geoffroykevan.resources.profile_not_logged_in
import com.example.swapcampus_geoffroykevan.resources.profile_role
import com.example.swapcampus_geoffroykevan.resources.profile_title
import com.example.swapcampus_geoffroykevan.ui.AppContainer
import com.example.swapcampus_geoffroykevan.ui.components.LoginRequired
import com.example.swapcampus_geoffroykevan.ui.i18n.currentLanguageTag
import com.example.swapcampus_geoffroykevan.ui.i18n.formatDate
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(onLoginClick: () -> Unit) {
    val authRepository = AppContainer.authRepository
    val user by authRepository.currentUser.collectAsState()
    val languageTag = currentLanguageTag()

    val account = user
    if (account == null) {
        LoginRequired(stringResource(Res.string.profile_not_logged_in), onLoginClick)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(stringResource(Res.string.profile_title), style = MaterialTheme.typography.headlineMedium)
        Text(
            stringResource(Res.string.profile_name, account.name),
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(stringResource(Res.string.profile_email, account.email))
        Text(stringResource(Res.string.profile_role, account.role.name))
        Text(stringResource(Res.string.profile_member_since, formatDate(account.createdAt, languageTag)))
        Button(
            onClick = { authRepository.logout() },
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(stringResource(Res.string.profile_logout))
        }
        LanguageSelector()
        Spacer(Modifier.height(32.dp))
        Text("SwapCampus", style = MaterialTheme.typography.titleSmall)
        Text(
            stringResource(Res.string.profile_credits),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
