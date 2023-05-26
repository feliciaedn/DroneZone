package com.example.prosjekt_team18.ui.viewmodels

import com.google.android.gms.maps.model.LatLng

/**
 * Dataklassen inneholder elementær informasjon for logikk og funksjonalitet
 * i appen. Dette gjelder særlig for hvilke Pages som skal vises når.
 */
data class ScreenUiState(
    val selectedLocation: LatLng,
    val showSearchBar: Boolean = false,
    val showWeather: Boolean = false,
	val showFeedback: Boolean = false,
    val showSheet: Sheet = Sheet.None,
    val showCurrentLocationData: Boolean = true,
)

enum class Sheet {
    Weather, Rules, Feedback, None
}