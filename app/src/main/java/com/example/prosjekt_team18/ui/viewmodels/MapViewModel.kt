package com.example.prosjekt_team18.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosjekt_team18.data.FeedbackCheck
import com.example.prosjekt_team18.data.maps.MapState
import com.example.prosjekt_team18.data.maps.SearchResult
import com.example.prosjekt_team18.data.resources.AirportData.airportNames
import com.example.prosjekt_team18.data.resources.AirportData.latCoordinates
import com.example.prosjekt_team18.data.resources.AirportData.lngCoordinates
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
import java.util.*

class MapViewModel(
//    private val weatherDataSource: WeatherDataSource,
//    private val sunDataSource: SunDataSource,
//    private val feedbackCheck: FeedbackCheck,
) : ViewModel() {
	private val weatherDataSource: WeatherDataSource = WeatherDataSource()
	private val sunDataSource: SunDataSource = SunDataSource()
	private val feedbackCheck: FeedbackCheck = FeedbackCheck()

	lateinit var placesClient: PlacesClient
	lateinit var fusedLocationClient: FusedLocationProviderClient

	val locationAutofill = mutableStateListOf<SearchResult>()

	var text by mutableStateOf("")

	var userLocation by mutableStateOf(LatLng(0.0, 0.0))
	var searchLatLong by mutableStateOf(LatLng(0.0, 0.0))

	var locationAddress by mutableStateOf(null)

	var hasLaunched: Boolean = false
	var hasLocation: Boolean = false

	var showMarker = mutableStateOf(false)
	var markerLocation: LatLng = LatLng(0.0, 0.0)

	private var job: Job? = null

	/* --- STATE FLOWS  --- */
	private val _mapUiState = MutableStateFlow(MapState())
	val mapUiState: StateFlow<MapState> = _mapUiState.asStateFlow()

	private val _screenUiState = MutableStateFlow(ScreenUiState(selectedLocation = userLocation))
	val screenUiState: StateFlow<ScreenUiState> = _screenUiState.asStateFlow()

	private val _sunWeatherUiState = MutableStateFlow(SunWeatherUiState())
	val sunWeatherUiState: StateFlow<SunWeatherUiState> = _sunWeatherUiState.asStateFlow()

	/* -------------------- */
//	init {
////		selectLocation(userLocation)
//	}

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
				showMarker.value = true
				markerLocation = searchLatLong
//				setShowCurrentLocationData(false)
			}
		}.addOnFailureListener {
			it.printStackTrace()
		}
	}

	fun selectLocation(location: LatLng) {
		_screenUiState.update { currentState ->
			currentState.copy(selectedLocation = location)
		}
		updateLocationData(location)
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
//		println("SHOWING: " + _screenUiState.value.showSheet)
	}

	fun setShowCurrentLocationData(show: Boolean) {
		_screenUiState.update { currentState ->
			currentState.copy(showCurrentLocationData = show)
		}
	}

	fun hideSheet() {
		_screenUiState.update { currentState ->
			currentState.copy(showSheet = Sheet.None)
		}
//		println("SHOWING: " + _screenUiState.value.showSheet)

	}

	fun updateLocationData(location: LatLng) {
		updateWeatherData(location)
		updateSunData(location)
		println("UPDATED LOCATION DATA TO $location")

	}

	private fun updateWeatherData(location: LatLng) {
		viewModelScope.launch(Dispatchers.IO) {
			_sunWeatherUiState.update { currentState ->
				try {
					val weatherModel: WeatherModel = weatherDataSource.getWeatherData(
						location.latitude,
						location.longitude
					)
					if (location != userLocation) {
						currentState.copy(status = Status.Success, pinnedCurrentWeather = weatherModel)
					} else {
						currentState.copy(status = Status.Success, currentWeather = weatherModel)

					}
				} catch (e: IOException) {
					currentState.copy(status = Status.Error)
				}
			}
		}
	}

	private fun updateSunData(location: LatLng) {
		viewModelScope.launch(Dispatchers.IO) {
			_sunWeatherUiState.update { currentState ->
				try {
					val sunData: SunData = sunDataSource.getSunData(
						latitude = location.latitude,
						longitude = location.longitude
					)

					if (location != userLocation) {
						currentState.copy(status = Status.Success, pinnedSunData = sunData)
					} else {
						currentState.copy(status = Status.Success, sunData = sunData)
					}
				} catch (e: IOException) {
					currentState.copy(status = Status.Error)
				}
			}
		}
	}

	fun enoughSunlight(forCurrentLocation: Boolean): Boolean {
		val sunData = if (forCurrentLocation) sunWeatherUiState.value.sunData else sunWeatherUiState.value.pinnedSunData

		val calendar = Calendar.getInstance().time

		if (sunData != null) {
			return feedbackCheck.enoughSunlight(sunData.sunrise.time, sunData.sunset.time, calendar)
		}
		return false
	}

	fun okRain(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.okRain(_sunWeatherUiState.value.currentWeather)
		return feedbackCheck.okRain(_sunWeatherUiState.value.pinnedCurrentWeather)
	}

	fun okSnow(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.okSnow(_sunWeatherUiState.value.currentWeather)
		return feedbackCheck.okSnow(_sunWeatherUiState.value.pinnedCurrentWeather)
	}

	fun okWind(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.okSnow(_sunWeatherUiState.value.currentWeather)
		return feedbackCheck.okWind(_sunWeatherUiState.value.pinnedCurrentWeather)

	}

	fun notInAirportZone(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.notInAirportZone(userLocation)
		return feedbackCheck.notInAirportZone(_screenUiState.value.selectedLocation)
	}

	fun checkApproval(
		sunlightCheck: Boolean,
		rainCheck: Boolean,
		snowCheck: Boolean,
		windCheck: Boolean,
		airportCheck: Boolean
	): Boolean {
		return feedbackCheck.checkApproval(sunlightCheck, rainCheck, snowCheck, windCheck, airportCheck)
	}

	fun airportLatCoordinates(): List<Double> {
		return latCoordinates
	}

	fun airportLngCoordinates(): List<Double> {
		return lngCoordinates
	}

	fun airportNames(): List<String> {
		return airportNames
	}
}