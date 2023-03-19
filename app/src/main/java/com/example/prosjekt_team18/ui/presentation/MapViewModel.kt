package com.example.prosjekt_team18.ui.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
	val state by mutableStateOf(MapState())
}