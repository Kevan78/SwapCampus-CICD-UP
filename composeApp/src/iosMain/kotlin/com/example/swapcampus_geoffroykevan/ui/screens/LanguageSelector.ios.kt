package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.language_ios_hint
import com.example.swapcampus_geoffroykevan.resources.language_title
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun LanguageSelector() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        Text(stringResource(Res.string.language_title), style = MaterialTheme.typography.titleMedium)
        Text(
            stringResource(Res.string.language_ios_hint),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
