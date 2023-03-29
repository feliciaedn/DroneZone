package com.example.prosjekt_team18.ui.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MapScreen(mapViewModel: MapViewModel, cameraPositionState: CameraPositionState) {

	val positionUiState by mapViewModel.mapUiState.collectAsState()

	Box {
		//Displayer google mappet
		GoogleMap(properties = positionUiState.properties, cameraPositionState = cameraPositionState)
	}
}
