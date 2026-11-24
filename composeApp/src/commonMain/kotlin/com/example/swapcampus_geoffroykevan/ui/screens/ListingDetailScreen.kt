package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_geoffroykevan.domain.ListingStatus
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.action_back
import com.example.swapcampus_geoffroykevan.resources.action_cancel
import com.example.swapcampus_geoffroykevan.resources.detail_buyer_name
import com.example.swapcampus_geoffroykevan.resources.detail_cancel_reservation
import com.example.swapcampus_geoffroykevan.resources.detail_cancel_sale
import com.example.swapcampus_geoffroykevan.resources.detail_category
import com.example.swapcampus_geoffroykevan.resources.detail_delete
import com.example.swapcampus_geoffroykevan.resources.detail_delete_message
import com.example.swapcampus_geoffroykevan.resources.detail_delete_title
import com.example.swapcampus_geoffroykevan.resources.detail_edit
import com.example.swapcampus_geoffroykevan.resources.detail_mark_sold
import com.example.swapcampus_geoffroykevan.resources.detail_missing
import com.example.swapcampus_geoffroykevan.resources.detail_photo
import com.example.swapcampus_geoffroykevan.resources.detail_posted
import com.example.swapcampus_geoffroykevan.resources.detail_reserve
import com.example.swapcampus_geoffroykevan.resources.detail_reserve_title
import com.example.swapcampus_geoffroykevan.resources.detail_seller
import com.example.swapcampus_geoffroykevan.resources.detail_status
import com.example.swapcampus_geoffroykevan.resources.seller_unknown
import com.example.swapcampus_geoffroykevan.ui.AppContainer
import com.example.swapcampus_geoffroykevan.ui.i18n.currentLanguageTag
import com.example.swapcampus_geoffroykevan.ui.i18n.description
import com.example.swapcampus_geoffroykevan.ui.i18n.displayName
import com.example.swapcampus_geoffroykevan.ui.i18n.formatDate
import com.example.swapcampus_geoffroykevan.ui.i18n.formatPrice
import com.example.swapcampus_geoffroykevan.ui.i18n.repositoryErrorText
import com.example.swapcampus_geoffroykevan.ui.viewmodel.ListingDetailViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListingDetailScreen(
    listingId: Int,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    viewModel: ListingDetailViewModel = viewModel {
        ListingDetailViewModel(AppContainer.listingRepository, AppContainer.authRepository, listingId)
    },
) {
    val listing by viewModel.listing.collectAsState()
    val currentUser by AppContainer.authRepository.currentUser.collectAsState()
    val languageTag = currentLanguageTag()
    var showReserveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val shownListing = listing
    if (shownListing == null) {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            Text(stringResource(Res.string.detail_missing))
            Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
                Text(stringResource(Res.string.action_back))
            }
        }
        return
    }

    val isOwner = currentUser?.id == shownListing.authorId
    val seller = viewModel.sellerName(shownListing.authorId).ifBlank {
        stringResource(Res.string.seller_unknown)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(shownListing.title, style = MaterialTheme.typography.headlineMedium)
        Text(formatPrice(shownListing.price, languageTag), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(Res.string.detail_category, shownListing.category.displayName()))
        Text(stringResource(Res.string.detail_status, shownListing.status.description(languageTag)))
        Text(stringResource(Res.string.detail_seller, seller))
        Text(
            stringResource(
                Res.string.detail_posted,
                formatDate(shownListing.createdAt, languageTag),
            ),
        )
        Text(stringResource(Res.string.detail_photo, shownListing.photoOrDefault()))
        Text(shownListing.description, style = MaterialTheme.typography.bodyLarge)

        repositoryErrorText(viewModel.errorMessage)?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        if (isOwner) {
            when (shownListing.status) {
                is ListingStatus.Available -> {
                    Button(onClick = { showReserveDialog = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.detail_reserve))
                    }
                    OutlinedButton(onClick = { onEdit(shownListing.id) }, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.detail_edit))
                    }
                }
                is ListingStatus.Reserved -> {
                    Button(onClick = viewModel::markAsSold, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.detail_mark_sold))
                    }
                    OutlinedButton(onClick = viewModel::cancel, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.detail_cancel_reservation))
                    }
                    OutlinedButton(onClick = { onEdit(shownListing.id) }, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.detail_edit))
                    }
                }
                is ListingStatus.Sold -> {
                    OutlinedButton(onClick = viewModel::cancel, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.detail_cancel_sale))
                    }
                }
            }
            OutlinedButton(onClick = { showDeleteDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(Res.string.detail_delete), color = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (showReserveDialog) {
        var buyer by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showReserveDialog = false },
            title = { Text(stringResource(Res.string.detail_reserve_title)) },
            text = {
                OutlinedTextField(
                    value = buyer,
                    onValueChange = { buyer = it },
                    label = { Text(stringResource(Res.string.detail_buyer_name)) },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.reserve(buyer)
                    showReserveDialog = false
                }) { Text(stringResource(Res.string.detail_reserve)) }
            },
            dismissButton = {
                TextButton(onClick = { showReserveDialog = false }) {
                    Text(stringResource(Res.string.action_cancel))
                }
            },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(Res.string.detail_delete_title)) },
            text = { Text(stringResource(Res.string.detail_delete_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.remove()
                    onBack()
                }) { Text(stringResource(Res.string.detail_delete)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(Res.string.action_cancel))
                }
            },
        )
    }
}
