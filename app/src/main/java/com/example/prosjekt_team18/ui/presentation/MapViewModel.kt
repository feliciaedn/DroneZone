package com.example.prosjekt_team18.ui.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel : ViewModel() {
	private val _mapUiState = MutableStateFlow(MapState())
	val mapUiState: StateFlow<MapState> = _mapUiState.asStateFlow()
}