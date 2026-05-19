package com.example.noteai.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteai.domain.model.Trip
import com.example.noteai.domain.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TripViewModel(
    private val repository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            repository.getAllTrips()
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message ?: "Terjadi kesalahan")
                }
                .collect { trips ->
                    _uiState.value = if (trips.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(trips)
                    }
                }
        }
    }

    fun insertTrip(trip: Trip) {
        viewModelScope.launch {
            try {
                repository.insertTrip(trip)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Gagal menyimpan trip")
            }
        }
    }

    fun updateTrip(trip: Trip) {
        viewModelScope.launch {
            try {
                repository.updateTrip(trip)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Gagal mengupdate trip")
            }
        }
    }

    fun deleteTrip(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteTrip(id)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Gagal menghapus trip")
            }
        }
    }
}
