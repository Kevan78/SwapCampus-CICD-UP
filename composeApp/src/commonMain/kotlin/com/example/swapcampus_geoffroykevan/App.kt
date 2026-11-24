@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.swapcampus_geoffroykevan

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.app_name
import com.example.swapcampus_geoffroykevan.resources.edit_login_required
import com.example.swapcampus_geoffroykevan.resources.nav_listings
import com.example.swapcampus_geoffroykevan.resources.nav_my_listings
import com.example.swapcampus_geoffroykevan.resources.nav_profile
import com.example.swapcampus_geoffroykevan.resources.post_login_required
import com.example.swapcampus_geoffroykevan.resources.topbar_detail
import com.example.swapcampus_geoffroykevan.resources.topbar_edit
import com.example.swapcampus_geoffroykevan.resources.topbar_listings
import com.example.swapcampus_geoffroykevan.resources.topbar_login
import com.example.swapcampus_geoffroykevan.resources.topbar_my_listings
import com.example.swapcampus_geoffroykevan.resources.topbar_post
import com.example.swapcampus_geoffroykevan.resources.topbar_profile
import com.example.swapcampus_geoffroykevan.ui.AppContainer
import com.example.swapcampus_geoffroykevan.ui.components.LoginRequired
import com.example.swapcampus_geoffroykevan.ui.navigation.DetailRoute
import com.example.swapcampus_geoffroykevan.ui.navigation.EditRoute
import com.example.swapcampus_geoffroykevan.ui.navigation.ListingsRoute
import com.example.swapcampus_geoffroykevan.ui.navigation.LoginRoute
import com.example.swapcampus_geoffroykevan.ui.navigation.MyListingsRoute
import com.example.swapcampus_geoffroykevan.ui.navigation.PostRoute
import com.example.swapcampus_geoffroykevan.ui.navigation.ProfileRoute
import com.example.swapcampus_geoffroykevan.ui.screens.ListingDetailScreen
import com.example.swapcampus_geoffroykevan.ui.screens.ListingFormScreen
import com.example.swapcampus_geoffroykevan.ui.screens.ListingsPostFab
import com.example.swapcampus_geoffroykevan.ui.screens.ListingsScreen
import com.example.swapcampus_geoffroykevan.ui.screens.LoginScreen
import com.example.swapcampus_geoffroykevan.ui.screens.MyListingsScreen
import com.example.swapcampus_geoffroykevan.ui.screens.ProfileScreen
import com.example.swapcampus_geoffroykevan.ui.theme.SwapCampusTheme
import com.example.swapcampus_geoffroykevan.ui.viewmodel.AuthViewModel
import com.example.swapcampus_geoffroykevan.ui.viewmodel.ListingsViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class TopLevelDestination(
    val route: Any,
    val label: StringResource,
    val icon: ImageVector,
)

private val topLevelDestinations = listOf(
    TopLevelDestination(ListingsRoute, Res.string.nav_listings, Icons.Filled.Home),
    TopLevelDestination(MyListingsRoute, Res.string.nav_my_listings, Icons.AutoMirrored.Filled.List),
    TopLevelDestination(ProfileRoute, Res.string.nav_profile, Icons.Filled.Person),
)

@Composable
fun App() {
    SwapCampusTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        val authViewModel: AuthViewModel = viewModel { AuthViewModel(AppContainer.authRepository) }
        val listingsViewModel: ListingsViewModel = viewModel { ListingsViewModel(AppContainer.listingRepository) }
        val currentUser by AppContainer.authRepository.currentUser.collectAsState()
        val isLoggedIn = currentUser != null
        val backStackEntry by navController.currentBackStackEntryAsState()
        val destination = backStackEntry?.destination

        val showBottomBar = topLevelDestinations.any { destination?.hasRoute(it.route::class) == true }
        val showPostFab = destination?.hasRoute(ListingsRoute::class) == true ||
            destination?.hasRoute(MyListingsRoute::class) == true

        val openPost = {
            if (isLoggedIn) navController.navigate(PostRoute) else navController.navigate(LoginRoute)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(
                                when {
                                    destination?.hasRoute(ListingsRoute::class) == true -> Res.string.topbar_listings
                                    destination?.hasRoute(MyListingsRoute::class) == true -> Res.string.topbar_my_listings
                                    destination?.hasRoute(ProfileRoute::class) == true -> Res.string.topbar_profile
                                    destination?.hasRoute(LoginRoute::class) == true -> Res.string.topbar_login
                                    destination?.hasRoute(PostRoute::class) == true -> Res.string.topbar_post
                                    destination?.hasRoute(EditRoute::class) == true -> Res.string.topbar_edit
                                    destination?.hasRoute(DetailRoute::class) == true -> Res.string.topbar_detail
                                    else -> Res.string.app_name
                                },
                            ),
                        )
                    },
                )
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        topLevelDestinations.forEach { item ->
                            NavigationBarItem(
                                selected = destination?.hasRoute(item.route::class) == true,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(item.icon, contentDescription = stringResource(item.label)) },
                                label = { Text(stringResource(item.label)) },
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (showPostFab) {
                    ListingsPostFab(onPostClick = openPost)
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Box(Modifier.fillMaxSize().padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = ListingsRoute,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    composable<ListingsRoute> {
                        ListingsScreen(
                            onListingClick = { id -> navController.navigate(DetailRoute(id)) },
                            viewModel = listingsViewModel,
                        )
                    }
                    composable<MyListingsRoute> {
                        MyListingsScreen(
                            onListingClick = { id -> navController.navigate(DetailRoute(id)) },
                            onLoginClick = { navController.navigate(LoginRoute) },
                            isLoggedIn = isLoggedIn,
                        )
                    }
                    composable<ProfileRoute> {
                        ProfileScreen(onLoginClick = { navController.navigate(LoginRoute) })
                    }
                    composable<LoginRoute> {
                        LoginScreen(
                            onLoggedIn = { navController.popBackStack() },
                            viewModel = authViewModel,
                        )
                    }
                    composable<DetailRoute> { entry ->
                        val id = entry.toRoute<DetailRoute>().id
                        ListingDetailScreen(
                            listingId = id,
                            onBack = { navController.popBackStack() },
                            onEdit = { editId -> navController.navigate(EditRoute(editId)) },
                        )
                    }
                    composable<PostRoute> {
                        if (isLoggedIn) {
                            ListingFormScreen(listingId = null, onDone = { navController.popBackStack() })
                        } else {
                            LoginRequired(stringResource(Res.string.post_login_required)) {
                                navController.navigate(LoginRoute)
                            }
                        }
                    }
                    composable<EditRoute> { entry ->
                        val id = entry.toRoute<EditRoute>().id
                        if (isLoggedIn) {
                            ListingFormScreen(listingId = id, onDone = { navController.popBackStack() })
                        } else {
                            LoginRequired(stringResource(Res.string.edit_login_required)) {
                                navController.navigate(LoginRoute)
                            }
                        }
                    }
                }
            }
        }
    }
}
