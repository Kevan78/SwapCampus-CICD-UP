package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.language_english
import com.example.swapcampus_geoffroykevan.resources.language_french
import com.example.swapcampus_geoffroykevan.resources.language_title
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun LanguageSelector() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(stringResource(Res.string.language_title), style = MaterialTheme.typography.titleMedium)
        androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = false,
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fr-FR"))
                },
                label = { Text(stringResource(Res.string.language_french)) },
            )
            FilterChip(
                selected = false,
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en-US"))
                },
                label = { Text(stringResource(Res.string.language_english)) },
            )
        }
    }
}
