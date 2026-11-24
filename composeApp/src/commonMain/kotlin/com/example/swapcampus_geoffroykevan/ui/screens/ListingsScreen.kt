package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_geoffroykevan.domain.Category
import com.example.swapcampus_geoffroykevan.domain.Listing
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.category_all
import com.example.swapcampus_geoffroykevan.resources.listings_empty
import com.example.swapcampus_geoffroykevan.resources.listings_post_cd
import com.example.swapcampus_geoffroykevan.resources.listings_search_label
import com.example.swapcampus_geoffroykevan.ui.AppContainer
import com.example.swapcampus_geoffroykevan.ui.components.ListingCard
import com.example.swapcampus_geoffroykevan.ui.i18n.displayName
import com.example.swapcampus_geoffroykevan.ui.viewmodel.ListingsUiState
import com.example.swapcampus_geoffroykevan.ui.viewmodel.ListingsViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListingsScreen(
    onListingClick: (Int) -> Unit,
    viewModel: ListingsViewModel = viewModel { ListingsViewModel(AppContainer.listingRepository) },
) {
    val uiState by viewModel.uiState.collectAsState()
    val keyword by viewModel.keyword.collectAsState()
    val selectedCategory by viewModel.category.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = keyword,
            onValueChange = viewModel::onKeywordChange,
            label = { Text(stringResource(Res.string.listings_search_label)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.onCategoryChange(null) },
                    label = { Text(stringResource(Res.string.category_all)) },
                )
            }
            items(Category.entries) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.onCategoryChange(category) },
                    label = { Text(category.displayName()) },
                )
            }
        }
        ListingsContent(
            uiState = uiState,
            emptyMessage = stringResource(Res.string.listings_empty),
            onListingClick = onListingClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun ListingsContent(
    uiState: ListingsUiState,
    emptyMessage: String,
    onListingClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showStatus: Boolean = false,
) {
    when (uiState) {
        is ListingsUiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(uiState.message, color = MaterialTheme.colorScheme.error)
        }
        is ListingsUiState.Success -> {
            if (uiState.listings.isEmpty()) {
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(emptyMessage, style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(uiState.listings, key = { it.id }) { listing: Listing ->
                        ListingCard(
                            listing = listing,
                            onClick = { onListingClick(listing.id) },
                            showStatus = showStatus,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListingsPostFab(onPostClick: () -> Unit, modifier: Modifier = Modifier) {
    androidx.compose.material3.FloatingActionButton(
        onClick = onPostClick,
        modifier = modifier,
    ) {
        Icon(Icons.Filled.Add, contentDescription = stringResource(Res.string.listings_post_cd))
    }
}
