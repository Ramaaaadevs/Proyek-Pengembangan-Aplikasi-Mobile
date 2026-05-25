package com.example.tripmate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripmate.presentation.screens.addedit.AddEditTripScreen
import com.example.tripmate.presentation.screens.ai.AIScreen
import com.example.tripmate.presentation.screens.detail.TripDetailScreen
import com.example.tripmate.presentation.screens.home.HomeScreen
import com.example.tripmate.presentation.screens.statistics.TripStatisticsScreen
import com.example.tripmate.presentation.screens.profile.ProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddTrip.route) },
                onNavigateToDetail = { tripId ->
                    navController.navigate(Screen.DetailTrip.createRoute(tripId))
                },
                onNavigateToAI = { navController.navigate(Screen.AIScreen.route) },
                onNavigateToStats = { navController.navigate(Screen.Statistics.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
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
                onNavigateToEdit = { id -> navController.navigate(Screen.EditTrip.createRoute(id)) }
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
