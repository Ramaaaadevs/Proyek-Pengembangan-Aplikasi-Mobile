package com.example.noteai.domain.repository

import com.example.noteai.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getAllTrips(): Flow<List<Trip>>
    fun getTripById(id: Long): Flow<Trip?>
    suspend fun insertTrip(trip: Trip)
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(id: Long)
}