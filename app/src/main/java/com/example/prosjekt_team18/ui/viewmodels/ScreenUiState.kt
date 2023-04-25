package com.example.prosjekt_team18.ui.viewmodels

import com.google.android.gms.maps.model.LatLng

data class ScreenUiState(
    val selectedLocation: LatLng,
    val showSearchBar: Boolean = false,
    val showWeather: Boolean = false,
	val showFeedback: Boolean = false,
    val showSheet: Sheet = Sheet.None,
)

enum class Sheet {
    Weather, Rules, Feedback, None
}