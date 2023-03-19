package com.example.prosjekt_team18.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType

@Composable
fun MapScreen(viewModel: MapViewModel) {
	Box {
		GoogleMap()
	}
}