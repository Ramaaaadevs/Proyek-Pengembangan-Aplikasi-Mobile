package com.example.noteai.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddTrip : Screen("add_trip")
    data object EditTrip : Screen("edit_trip/{tripId}") {
        fun createRoute(tripId: Long) = "edit_trip/$tripId"
    }
}
