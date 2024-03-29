package com.example.prosjekt_team18.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.example.prosjekt_team18.ui.viewmodels.Sheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Konstruerer en navigasjonsmany nederst på skjermen for tilgang til appens Pages og Søkefunksjon.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationBar(
	mapViewModel: MapViewModel,
	modalSheetState: ModalBottomSheetState,
	coroutineScope: CoroutineScope,
) {

    var selectedItem by remember { mutableStateOf("") }
    val items = listOf("Search", "Feedback", "Weather", "Rules")

    BottomAppBar(modifier = Modifier.fillMaxWidth().shadow(elevation = 4.dp), containerColor = Color(230, 230, 240)) {
		Column(modifier = Modifier.fillMaxSize()) {


			Row {
					NavigationBarItem(
						icon = {
							Image(
								modifier = Modifier.size(32.dp),
								painter = painterResource(id = R.drawable.icons8_search_96),
								contentDescription = items[0]
							)
						},
						selected = selectedItem == items[0],
						onClick = {
							selectedItem = if (selectedItem != items[0]) {
								items[0]
							} else {
								""
							}
							mapViewModel.toggleShowSearchBar()
						}
					)
					NavigationBarItem(
						icon = {
							Image(
								modifier = Modifier.size(32.dp),
								painter = painterResource(id = R.drawable.icons8_pass_fail_96),
								contentDescription = items[1]
							)
						},
						selected =
						selectedItem == items[1],
						onClick = {
							mapViewModel.setShowCurrentLocationData(!mapViewModel.showMarker.value)
							mapViewModel.showSheet(Sheet.Feedback)
							coroutineScope.launch {
								modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
							}
						}
					)
					NavigationBarItem(
						icon = {
							Image(
								modifier = Modifier.size(32.dp),
								painter = painterResource(id = R.drawable.icons8_sun_96),
								contentDescription = items[2]
							)
						},
						selected = selectedItem == items[2],
						onClick = {
							mapViewModel.setShowCurrentLocationData(!mapViewModel.showMarker.value)
							mapViewModel.showSheet(Sheet.Weather)
							coroutineScope.launch {
								modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
							}
						}
					)
					NavigationBarItem(
						icon = {
							Image(
								modifier = Modifier.size(32.dp),
								painter = painterResource(id = R.drawable.icons8_literature_96),
								contentDescription = items[3]
							)
						},
						selected = selectedItem == items[3],
						onClick = {
							mapViewModel.showSheet(Sheet.Rules)
							coroutineScope.launch {
								modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
							}
						}
					)
				}
			}
		}
    }
