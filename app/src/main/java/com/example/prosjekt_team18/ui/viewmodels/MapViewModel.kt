package com.example.prosjekt_team18.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosjekt_team18.data.maps.MapState
import com.example.prosjekt_team18.data.weather.WeatherDataSource
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MapViewModel(val weatherDataSource: WeatherDataSource) : ViewModel() {
	private val _mapUiState = MutableStateFlow(MapState())
	val mapUiState: StateFlow<MapState> = _mapUiState.asStateFlow()

	private val _screenUiState = MutableStateFlow(ScreenUiState())
	val screenUiState: StateFlow<ScreenUiState> = _screenUiState.asStateFlow()

	private val _weatherUiState = MutableStateFlow(WeatherUiState())
	val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()


	fun toggleShowSearchBar() {
		val showing: Boolean = _screenUiState.value.showSearchBar
		_screenUiState.update { currentState ->
			currentState.copy(showSearchBar = !showing, showRuleSheet = false)
		}
	}

	fun toggleShowRuleSheet() {
		val showing: Boolean = _screenUiState.value.showRuleSheet
		_screenUiState.update { currentState ->
			currentState.copy(showRuleSheet = !showing)
		}
	}

	fun showWeather() {
		_screenUiState.update { currentState ->
			currentState.copy(showWeather = true)
		}
	}

	fun hideWeather() {
		_screenUiState.update { currentState ->
			currentState.copy(showWeather = false)
		}
	}

	fun updateWeatherData(userLocation: LatLng) {
		viewModelScope.launch(Dispatchers.IO) {
			_weatherUiState.update { currentState ->
				try {
					val currentWeather: WeatherModel = weatherDataSource.getWeatherData(userLocation.latitude, userLocation.longitude)
					currentState.copy(status = Status.Success, currentWeather = currentWeather)
				} catch (e: IOException) {
					currentState.copy(status = Status.Error)
				}
			}
		}
	}
}