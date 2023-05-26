package com.example.prosjekt_team18.data.maps

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

/**
 * Dataklassen inneholder informasjon om kartets oppsett, samt brukers lokasjon.
 * Denne informasjonen brukes videre i MapViewModel som en State
 */
data class MapState(
	var properties: MapProperties = MapProperties(mapType = MapType.TERRAIN),
	var currentLocation: LocationDetails = LocationDetails(0.0, 0.0)
)