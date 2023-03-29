package com.example.prosjekt_team18.ui.presentation

import android.location.Location
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

data class MapState(
	var properties: MapProperties = MapProperties(mapType = MapType.TERRAIN),
	var currentLocation: LocationDetails = LocationDetails(0.0, 0.0)
)