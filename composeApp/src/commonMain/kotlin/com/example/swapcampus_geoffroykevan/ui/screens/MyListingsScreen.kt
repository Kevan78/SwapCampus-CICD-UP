package com.example.swapcampus_geoffroykevan.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.listings_empty
import com.example.swapcampus_geoffroykevan.resources.my_listings_login_required
import com.example.swapcampus_geoffroykevan.ui.AppContainer
import com.example.swapcampus_geoffroykevan.ui.components.LoginRequired
import com.example.swapcampus_geoffroykevan.ui.viewmodel.MyListingsViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyListingsScreen(
    onListingClick: (Int) -> Unit,
    onLoginClick: () -> Unit,
    isLoggedIn: Boolean,
    viewModel: MyListingsViewModel = viewModel {
        MyListingsViewModel(AppContainer.listingRepository, AppContainer.authRepository)
    },
) {
    if (!isLoggedIn) {
        LoginRequired(stringResource(Res.string.my_listings_login_required), onLoginClick)
        return
    }
    val uiState by viewModel.uiState.collectAsState()
    ListingsContent(
        uiState = uiState,
        emptyMessage = stringResource(Res.string.listings_empty),
        onListingClick = onListingClick,
        modifier = Modifier.fillMaxSize(),
        showStatus = true,
    )
}
