package com.example.noteai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteai.presentation.screens.addedit.AddEditTripScreen
import com.example.noteai.presentation.screens.detail.TripDetailScreen
import com.example.noteai.presentation.screens.home.HomeScreen

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
                onNavigateToAdd = {
                    navController.navigate(Screen.AddTrip.route)
                },
                onNavigateToEdit = { tripId ->
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

        composable(
            route = Screen.DetailTrip.route,
            arguments = listOf(
                navArgument("tripId") { type = NavType.LongType }
            )
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
            arguments = listOf(
                navArgument("tripId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong("tripId")
            AddEditTripScreen(
                tripId = tripId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
