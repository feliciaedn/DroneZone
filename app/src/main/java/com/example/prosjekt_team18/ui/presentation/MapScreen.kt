package com.example.prosjekt_team18.ui.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapScreen(viewModel: MapViewModel) {
	Box {
		//Displayer google mappet
		GoogleMap()
	}
}