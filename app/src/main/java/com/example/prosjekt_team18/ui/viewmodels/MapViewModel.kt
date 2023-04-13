package com.example.prosjekt_team18.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosjekt_team18.data.maps.MapState
import com.example.prosjekt_team18.data.weather.WeatherDataSource
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.example.prosjekt_team18.ui.screens.AutocompleteResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

	lateinit var placesClient: PlacesClient
	val locationAutofill = mutableStateListOf<AutocompleteResult>()

	var text by mutableStateOf("")

	var currentLatLong by mutableStateOf(LatLng(0.0, 0.0))

	private var job: Job? = null

	fun searchPlaces(query: String) {
		job?.cancel()
		locationAutofill.clear()
		job = viewModelScope.launch {
			val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
			placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
				locationAutofill += response.autocompletePredictions.map {
					AutocompleteResult(
						it.getFullText(null).toString(), it.placeId
					)
				}
			}.addOnFailureListener {
				it.printStackTrace()
				println(it.cause)
				println(it.message)
			}
		}
	}

	fun getCoordinates(result: AutocompleteResult) {
		val placeFields = listOf(Place.Field.LAT_LNG)
		val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
		placesClient.fetchPlace(request).addOnSuccessListener {
			if (it != null) {
				currentLatLong = it.place.latLng!!
				println("inni getCoordinates: " + currentLatLong.latitude + ", " + currentLatLong.longitude)
			}
		}.addOnFailureListener {
			it.printStackTrace()
		}
	}


	fun toggleShowSearchBar() {
		val showing = _screenUiState.value.showSearchBar
		_screenUiState.update { currentState ->
			currentState.copy(showSearchBar = !showing)
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