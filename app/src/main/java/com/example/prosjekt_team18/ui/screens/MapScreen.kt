package com.example.prosjekt_team18.ui.screens


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.ui.components.NavigationBar
import com.example.prosjekt_team18.ui.components.PopupDialog
import com.example.prosjekt_team18.ui.components.SearchBar
import com.example.prosjekt_team18.ui.components.SegmentedControl
import com.example.prosjekt_team18.ui.pages.FeedbackPage
import com.example.prosjekt_team18.ui.pages.WeatherPage
import com.example.prosjekt_team18.ui.viewmodels.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition",
	"UnusedMaterialScaffoldPaddingParameter"
)
@Composable
fun MainScreen(mapViewModel: MapViewModel, cameraPositionState: CameraPositionState, userLocation: LatLng, permissionGranted: Boolean, context: Context) {
	val screenUiState = mapViewModel.screenUiState.collectAsState()
	val sunWeatherUiState = mapViewModel.sunWeatherUiState.collectAsState()

	mapViewModel.updateLocationData(userLocation)

	val coroutineScope = rememberCoroutineScope()

	val modalSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		confirmStateChange = { it != ModalBottomSheetValue.Expanded },
		skipHalfExpanded = true,
	)

	if (screenUiState.value.showSheet == Sheet.None && modalSheetState.isVisible){
		coroutineScope.launch {
			modalSheetState.hide()
			mapViewModel.hideSheet()
		}
	}

	val modifier = Modifier.height((LocalConfiguration.current.screenHeightDp*0.85).dp)

	ModalBottomSheetLayout(
		modifier = Modifier.fillMaxHeight(),
		sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
		sheetState = modalSheetState,
		sheetContent = {
			Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
				IconButton(
					onClick = {
						coroutineScope.launch {
							modalSheetState.hide()
							mapViewModel.hideSheet()
						}
					},
				) {
					androidx.compose.material.Icon(
						Icons.Default.Close,
						contentDescription = "lukk siden",
						tint = MaterialTheme.colors.onSurface
					)
				}
				Spacer(Modifier.weight(1f))
				if ((screenUiState.value.showSheet == Sheet.Weather ||  screenUiState.value.showSheet == Sheet.Feedback)
					&& sunWeatherUiState.value.status == Status.Success) {
//					showCurrentLocationData = !mapViewModel.showMarker.value
					SegmentedControl(
						items = listOf("Data for min lokasjon", "Data for markert lokasjon"),
//						if (mapViewModel.showMarker.value) 1 else 0
						defaultSelectedItemIndex = if (screenUiState.value.showCurrentLocationData) 0 else 1,
						itemWidth = 50.dp,
						cornerRadius = 100,
						color =  R.color.dark_blue,
						onItemSelection = { selectedItemIndex ->
							mapViewModel.setShowCurrentLocationData(selectedItemIndex == 0)
						},
						pinnedLocation = mapViewModel.showMarker.value
					)
				}
			}

			Column(
				modifier =  modifier
					//.fillMaxWidth()
					.padding(top = 0.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)

			) {
				if (screenUiState.value.showSheet == Sheet.Rules) {
					val modifier = Modifier
					RulePage(modifier)

				} else if (screenUiState.value.showSheet == Sheet.Weather) {

					if (sunWeatherUiState.value.status == Status.Success) {
							if (screenUiState.value.showCurrentLocationData) {
								WeatherPage(sunWeatherUiState, context, userLocation, true)
							} else {
								WeatherPage(sunWeatherUiState, context, screenUiState.value.selectedLocation, false)
							}

					} else if (sunWeatherUiState.value.status == Status.Error){
						PopupDialog(
							title = "Kunne ikke laste inn værdata",
							description = "Sjekk at du er tilkoblet internet og prøv igjen."
						)
					}
//					WeatherPage(sunWeatherUiState, context, screenUiState.value.selectedLocation!!)
//					println("SHOWING WEATHER PAGE for location ${screenUiState.value.selectedLocation}")
//					println("weathermodel: " + sunWeatherUiState.value.currentWeather)

				} else if (screenUiState.value.showSheet == Sheet.Feedback) {
					if (sunWeatherUiState.value.status == Status.Success) {
						if (screenUiState.value.showCurrentLocationData) {
							FeedbackPage(mapViewModel, showCurrentLocation = true)
						} else {
							FeedbackPage(mapViewModel, showCurrentLocation = false)
						}

					} else if (sunWeatherUiState.value.status == Status.Error){
						PopupDialog(
							title = "Kunne ikke laste inn værdata",
							description = "Værdata for valgt lokasjon trengs for beregning av " +
									"flyvetillatelse. Sjekk at du er tilkoblet internet og prøv igjen."
						)
					}
//					println("SHOWING FEEDBACK PAGE for location ${screenUiState.value.selectedLocation}")
				}

			}
		}
	) {
        Scaffold(
            scaffoldState = rememberScaffoldState(),
            ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Column() {
					MapScreen(mapViewModel,
						cameraPositionState,
						userLocation,
						permissionGranted,
						context,
						screenUiState,
						sunWeatherUiState,
						modalSheetState,
						coroutineScope)

				}
            }
        }

	}
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MapScreen(mapViewModel: MapViewModel,
			  cameraPositionState: CameraPositionState,
			  userLocation: LatLng,
			  permissionGranted: Boolean,
			  context: Context,
			  screenUiState: State<ScreenUiState>,
			  sunWeatherUiState: State<SunWeatherUiState>,
			  modalSheetState: ModalBottomSheetState,
			  coroutineScope: CoroutineScope,
) {

	val positionUiState by mapViewModel.mapUiState.collectAsState()

	var lagre2 = remember { mutableStateOf("Trykk for å sjekke værmelding") }

	Column(modifier = Modifier.fillMaxSize()) {
		Box(modifier = Modifier.weight(15f)) {
			//Displayer google mappet
			GoogleMap(
				properties = positionUiState.properties,
				cameraPositionState = cameraPositionState,
				onMapClick = {
					if(mapViewModel.showMarker.value) {
						lagre2.value = "Trykk for å sjekke værmelding"
					}
					mapViewModel.showMarker.value = !mapViewModel.showMarker.value
					mapViewModel.markerLocation = it
				}
			) {
				if(mapViewModel.showMarker.value) {
					val markerState = rememberMarkerState(position = mapViewModel.markerLocation)
					mapViewModel.selectLocation(mapViewModel.markerLocation)
					markerState.showInfoWindow()
					Marker(
						state = markerState,
						title = "Lokasjon",
						snippet = lagre2.value,
						icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
						onInfoWindowClick = {
							println("CLICKED INFO WINDOW")
							mapViewModel.setShowCurrentLocationData(false)
							mapViewModel.showSheet(Sheet.Weather)
							coroutineScope.launch {
								modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
							}

						}
					)

				}

				// DRAW AIRPORTS

				val latCoordinates = mapViewModel.airportLatCoordinates()
				val lngCoordinates = mapViewModel.airportLngCoordinates()

				for (i in latCoordinates.indices) {
					Marker(
						state = rememberMarkerState(
							position = LatLng(
								latCoordinates[i],
								lngCoordinates[i]
							)
						),
						title = mapViewModel.airportNames()[i],
						//Legge inn ikon for flyplass i stedet?
						icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
					)
					Circle(
						center = LatLng(latCoordinates[i], lngCoordinates[i]),
						fillColor = Color(0x80FF0000),
						strokeWidth = 0F,
						radius = 5000.0,
						visible = true
					)
				}

			}

			if (screenUiState.value.showSearchBar) {
				Surface(
					modifier = Modifier
					.align(Alignment.TopCenter)
					.padding(8.dp)
					.fillMaxWidth()
						.shadow(elevation = 10.dp),
					color = Color.White,
					shape = RoundedCornerShape(16.dp)
				) {
					SearchBar(mapViewModel)
				}

			}

			if (userLocation == LatLng(0.0, 0.0)) {
				IconButton(
					modifier = Modifier
						.fillMaxWidth()
						.wrapContentWidth(Alignment.End)
						.padding(top = 7.dp, end = 7.dp)
						.shadow(20.dp),
					enabled = !permissionGranted,
					content = {
						Icon(
							modifier = Modifier.size(23.dp),
							tint = Color.Black.copy(0.6f),
							painter = painterResource(id = R.drawable.icons8_no_gps_96),
							contentDescription = "GPS disabled"
						)
					},
					onClick = {
						Toast.makeText(
							context,
							"GPS not enabled",
							Toast.LENGTH_SHORT
						).show()
					},
					colors = IconButtonDefaults.filledIconButtonColors(Color.White.copy(alpha = 0.7f))
				)
			}
		}

		NavigationBar(
			Modifier.padding(12.dp),
			NavigationBarDefaults.containerColor,
			12.dp,
			NavigationBarDefaults.windowInsets,
			mapViewModel, sunWeatherUiState,
			context,
			userLocation,
			modalSheetState,
			coroutineScope,
		)
		}
}



