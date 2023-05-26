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

/**
 * Klassen inneholder essensiell data og funksjoner for funksjonalitet i appen.
 */
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

	/**
	 * Funksjonen finner forslag til steder basert på en input-streng, og legger
	 * forslagene samlet i en liste, locationAutofill, som søkefunksjonen kan
	 * vise frem for brukeren.
	 */
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

	/**
	 * Funksjonen får inn et søkeresultat for en lokasjon som parameter, og henter inn
	 * breddegrad og lengdegrad for dette søket.
	 */
	fun getCoordinates(result: SearchResult) {
		val placeFields = listOf(Place.Field.LAT_LNG)
		val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
		placesClient.fetchPlace(request).addOnSuccessListener {
			if (it != null) {
				searchLatLong = it.place.latLng!!
				println("inni getCoordinates: " + searchLatLong.latitude + ", " + searchLatLong.longitude)
				showMarker.value = true
				markerLocation = searchLatLong
			}
		}.addOnFailureListener {
			it.printStackTrace()
		}
	}

	/**
	 * Funksjonen får inn en lokasjon som parameter, og setter screenUiState
	 * sin valgte lokasjon til å være denne, samt oppdatere denne dataen.
	 */
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

	/**
	 * Funksjonen kaller på relevante metoder for å oppdatere værdataen og
	 * soldataen for en ny lokasjon.
	 */
	fun updateLocationData(location: LatLng) {
		updateWeatherData(location)
		updateSunData(location)
		println("UPDATED LOCATION DATA TO $location")

	}

	/**
	 * Funksjonen oppdaterer sunWeatherUiState sin værdata for en ny lokasjon. Dette
	 * gjøres ved å iverksette metodekall for å innhente oppdatert værdata fra
	 * vær-APIet til MET.
	 */
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

	/**
	 * Funksjonen oppdaterer sunWeatherUiState sin soldata for en ny lokasjon. Dette
	 * gjøres ved å iverksette metodekall for å innhente oppdatert soldata fra
	 * sol-APIet til MET.
	 */
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

	/**
	 * Funksjonen iverksetter metodekall mot funksjonen som avgjør om brukeren
	 * befinner seg på en lokasjon med nok sollys til å fly drone. Returnerer
	 * false dersom sunData er null, og ellers returnerer den videre true/false
	 * basert på returverdien fra feedbackCheck.enoughSunlight()
	 */
	fun enoughSunlight(forCurrentLocation: Boolean): Boolean {
		val sunData = if (forCurrentLocation) sunWeatherUiState.value.sunData else sunWeatherUiState.value.pinnedSunData

		val calendar = Calendar.getInstance().time

		if (sunData != null) {
			return feedbackCheck.enoughSunlight(sunData.sunrise.time, sunData.sunset.time, calendar)
		}
		return false
	}

	/**
	 * Funksjonen iverksetter metodekall mot funksjonen for å sjekke om det er lite
	 * nok regn på en lokasjon for å kunne fly drone. Returnerer videre true/false
	 * avhengig av hva feedbackCheck.okRain() returnerer
	 */
	fun okRain(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.okRain(_sunWeatherUiState.value.currentWeather)
		return feedbackCheck.okRain(_sunWeatherUiState.value.pinnedCurrentWeather)
	}

	/**
	 * Funksjonen iverksetter metodekall mot funksjonen for å sjekke om det er lite
	 * nok snø på en lokasjon for å kunne fly drone. Returnerer videre true/false
	 * avhengig av hva feedbackCheck.okSnow() returnerer
	 */
	fun okSnow(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.okSnow(_sunWeatherUiState.value.currentWeather)
		return feedbackCheck.okSnow(_sunWeatherUiState.value.pinnedCurrentWeather)
	}

	/**
	 * Funksjonen iverksetter metodekall mot funksjonen for å sjekke om det er lite
	 * nok vind på en lokasjon for å kunne fly drone. Returnerer videre true/false
	 * avhengig av hva feedbackCheck.okWind() returnerer
	 */
	fun okWind(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.okSnow(_sunWeatherUiState.value.currentWeather)
		return feedbackCheck.okWind(_sunWeatherUiState.value.pinnedCurrentWeather)

	}

	/**
	 * Funksjonen iverksetter metodekall mot funksjonen for å sjekke om en bruker
	 * befinner seg langt nok unna en flyplass til å kunne fly drone. Returnerer
	 * videre true/false avhengig av hva feedbackCheck.okRain() returnerer.
	 */
	fun notInAirportZone(forCurrentLocation: Boolean): Boolean {
		if (forCurrentLocation)
			return feedbackCheck.notInAirportZone(userLocation)
		return feedbackCheck.notInAirportZone(_screenUiState.value.selectedLocation)
	}

	/**
	 * Funksjonen iverksetter metodekall mot funksjonen som avgjør en samlet
	 * vurdering på om brukeren kan fly drone på en lokasjon. Returnerer videre
	 * true/false avhengig av hva feedbackCheck.okRain() returnerer.
	 */
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