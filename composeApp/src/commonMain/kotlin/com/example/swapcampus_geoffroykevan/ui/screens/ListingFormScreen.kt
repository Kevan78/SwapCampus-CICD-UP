package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_geoffroykevan.domain.Category
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.action_back
import com.example.swapcampus_geoffroykevan.resources.error_must_login
import com.example.swapcampus_geoffroykevan.resources.form_category
import com.example.swapcampus_geoffroykevan.resources.form_description
import com.example.swapcampus_geoffroykevan.resources.form_edit_title
import com.example.swapcampus_geoffroykevan.resources.form_forbidden
import com.example.swapcampus_geoffroykevan.resources.form_photo_url
import com.example.swapcampus_geoffroykevan.resources.form_post
import com.example.swapcampus_geoffroykevan.resources.form_post_title
import com.example.swapcampus_geoffroykevan.resources.form_price
import com.example.swapcampus_geoffroykevan.resources.form_save
import com.example.swapcampus_geoffroykevan.resources.form_title
import com.example.swapcampus_geoffroykevan.ui.AppContainer
import com.example.swapcampus_geoffroykevan.ui.i18n.displayName
import com.example.swapcampus_geoffroykevan.ui.i18n.repositoryErrorText
import com.example.swapcampus_geoffroykevan.ui.viewmodel.ListingFormViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListingFormScreen(
    listingId: Int?,
    onDone: () -> Unit,
    viewModel: ListingFormViewModel = viewModel {
        ListingFormViewModel(AppContainer.listingRepository, AppContainer.authRepository, listingId)
    },
) {
    if (viewModel.forbidden) {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            Text(stringResource(Res.string.form_forbidden))
            Button(onClick = onDone, modifier = Modifier.padding(top = 16.dp)) {
                Text(stringResource(Res.string.action_back))
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            stringResource(if (viewModel.isEditing) Res.string.form_edit_title else Res.string.form_post_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        OutlinedTextField(
            value = viewModel.title,
            onValueChange = { viewModel.title = it },
            label = { Text(stringResource(Res.string.form_title)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text(stringResource(Res.string.form_description)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = viewModel.priceText,
            onValueChange = { viewModel.priceText = it },
            label = { Text(stringResource(Res.string.form_price)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(stringResource(Res.string.form_category))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Category.entries) { category ->
                FilterChip(
                    selected = viewModel.category == category,
                    onClick = { viewModel.category = category },
                    label = { Text(category.displayName()) },
                )
            }
        }
        OutlinedTextField(
            value = viewModel.photoUrl,
            onValueChange = { viewModel.photoUrl = it },
            label = { Text(stringResource(Res.string.form_photo_url)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        if (viewModel.mustLogin) {
            Text(stringResource(Res.string.error_must_login), color = MaterialTheme.colorScheme.error)
        }
        repositoryErrorText(viewModel.errorMessage)?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = { if (viewModel.save()) onDone() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(if (viewModel.isEditing) Res.string.form_save else Res.string.form_post))
        }
    }
}
