package com.example.tripmate.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripmate.presentation.screens.addedit.AddEditTripScreen
import com.example.tripmate.presentation.screens.ai.AIScreen
import com.example.tripmate.presentation.screens.detail.TripDetailScreen
import com.example.tripmate.presentation.screens.home.HomeScreen
import com.example.tripmate.presentation.screens.profile.ProfileScreen
import com.example.tripmate.presentation.screens.statistics.TripStatisticsScreen

// Route-route yang TIDAK menampilkan bottom navbar
private val routesWithoutBottomBar = setOf(
    Screen.AddTrip.route,
    Screen.DetailTrip.route,
    Screen.EditTrip.route
)

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // Sembunyikan navbar kalau di detail/add/edit
    val showBottomBar = routesWithoutBottomBar.none { pattern ->
        currentRoute == pattern || currentRoute?.startsWith(pattern.substringBefore("{")) == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomNavItem.items.forEach { item ->
                        val selected = currentDestination?.hierarchy
                            ?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop ke start destination supaya back stack tidak numpuk
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToAdd = { navController.navigate(Screen.AddTrip.route) },
                    onNavigateToDetail = { tripId ->
                        navController.navigate(Screen.DetailTrip.createRoute(tripId))
                    }
                )
            }

            composable(Screen.AddTrip.route) {
                AddEditTripScreen(
                    tripId = null,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.AIScreen.route) {
                AIScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.DetailTrip.route,
                arguments = listOf(navArgument("tripId") { type = NavType.LongType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getLong("tripId") ?: 0L
                TripDetailScreen(
                    tripId = tripId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { id ->
                        navController.navigate(Screen.EditTrip.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.EditTrip.route,
                arguments = listOf(navArgument("tripId") { type = NavType.LongType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getLong("tripId")
                AddEditTripScreen(
                    tripId = tripId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Statistics.route) {
                TripStatisticsScreen(onNavigateBack = { navController.popBackStack() })
            }

            composable(Screen.Profile.route) {
                ProfileScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
