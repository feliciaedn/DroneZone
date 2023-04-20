package com.example.prosjekt_team18.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosjekt_team18.data.maps.MapState
import com.example.prosjekt_team18.data.maps.SearchResult
import com.example.prosjekt_team18.data.sunrise.SunData
import com.example.prosjekt_team18.data.sunrise.SunDataSource
import com.example.prosjekt_team18.data.weather.WeatherDataSource
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
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

class MapViewModel(
	private val weatherDataSource: WeatherDataSource,
	private val sunDataSource: SunDataSource,
) : ViewModel() {

	private val _mapUiState = MutableStateFlow(MapState())
	val mapUiState: StateFlow<MapState> = _mapUiState.asStateFlow()

	private val _screenUiState = MutableStateFlow(ScreenUiState())
	val screenUiState: StateFlow<ScreenUiState> = _screenUiState.asStateFlow()

	private val _sunWeatherUiState = MutableStateFlow(SunWeatherUiState())
	val sunWeatherUiState: StateFlow<SunWeatherUiState> = _sunWeatherUiState.asStateFlow()

	lateinit var placesClient: PlacesClient
	lateinit var fusedLocationClient: FusedLocationProviderClient

	val locationAutofill = mutableStateListOf<SearchResult>()

	var text by mutableStateOf("")

	var userLocation by mutableStateOf(LatLng(0.0, 0.0))
	var searchLatLong by mutableStateOf(LatLng(0.0, 0.0))

	var hasLaunched: Boolean = false
	var hasLocation: Boolean = false

	var showMarker = mutableStateOf(false)
	// MARKER HINDA
	var markerLocation: LatLng = LatLng(0.0, 0.0)

	private var job: Job? = null

	fun searchPlaces(query: String) {
		job?.cancel()
		locationAutofill.clear()
		job = viewModelScope.launch {
			val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
			placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
				locationAutofill += response.autocompletePredictions.map {
					SearchResult(
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

	fun getCoordinates(result: SearchResult) {
		val placeFields = listOf(Place.Field.LAT_LNG)
		val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
		placesClient.fetchPlace(request).addOnSuccessListener {
			if (it != null) {
				searchLatLong = it.place.latLng!!
				println("inni getCoordinates: " + searchLatLong.latitude + ", " + searchLatLong.longitude)
			}
		}.addOnFailureListener {
			it.printStackTrace()
		}
	}


	fun toggleShowSearchBar() {
		val showing: Boolean = _screenUiState.value.showSearchBar
		_screenUiState.update { currentState ->
			currentState.copy(showSearchBar = !showing, showSheet = Sheet.None)
		}
	}

	fun showSheet(sheet: Sheet) {
		_screenUiState.update { currentState ->
			currentState.copy(showSheet = sheet)
		}
		println("SHOWING: " + _screenUiState.value.showSheet)
	}

	fun hideSheet() {
		_screenUiState.update { currentState ->
			currentState.copy(showSheet = Sheet.None)
		}
		println("SHOWING: " + _screenUiState.value.showSheet)

	}


	fun updateWeatherData(userLocation: LatLng) {
		viewModelScope.launch(Dispatchers.IO) {
			_sunWeatherUiState.update { currentState ->
				try {
					val currentWeather: WeatherModel = weatherDataSource.getWeatherData(
						userLocation.latitude,
						userLocation.longitude
					)
					currentState.copy(status = Status.Success, currentWeather = currentWeather)
				} catch (e: IOException) {
					currentState.copy(status = Status.Error)
				}
			}
		}
	}

	fun updateSunData(userLocation: LatLng) {
		viewModelScope.launch(Dispatchers.IO) {
			_sunWeatherUiState.update { currentState ->
				try {
					val sunData: SunData = sunDataSource.getSunData(
						latitude = userLocation.latitude,
						longitude = userLocation.longitude
					)

					currentState.copy(status = Status.Success, sunData = sunData)
				} catch (e: IOException) {
					currentState.copy(status = Status.Error)
				}
			}
		}
	}
}