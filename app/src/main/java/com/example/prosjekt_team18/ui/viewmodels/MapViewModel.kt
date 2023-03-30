package com.example.prosjekt_team18.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.prosjekt_team18.data.maps.MapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MapViewModel : ViewModel() {
	private val _mapUiState = MutableStateFlow(MapState())
	val mapUiState: StateFlow<MapState> = _mapUiState.asStateFlow()

	private val _screenUiState = MutableStateFlow(ScreenUiState())
	val screenUiState: StateFlow<ScreenUiState> = _screenUiState.asStateFlow()

	fun toggleShowSearchBar() {
		val showing = _screenUiState.value.showSearchBar
		_screenUiState.update { currentState ->
			currentState.copy(showSearchBar = !showing)
		}
	}


}