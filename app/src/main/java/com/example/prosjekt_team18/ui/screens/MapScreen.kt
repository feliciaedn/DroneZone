package com.example.prosjekt_team18.ui.screens


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.example.prosjekt_team18.ui.viewmodels.ScreenUiState
import com.example.prosjekt_team18.ui.viewmodels.Sheet
import com.example.prosjekt_team18.ui.viewmodels.SunWeatherUiState

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

	mapViewModel.updateLocationData()


	val coroutineScope = rememberCoroutineScope()

	val modalSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		confirmStateChange = { it != ModalBottomSheetValue.Expanded },
		skipHalfExpanded = true,
	)

//	if(screenUiState.value.showSheet != Sheet.None){
//		coroutineScope.launch {
//			modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
//		}
//	} else {
	if (screenUiState.value.showSheet == Sheet.None && modalSheetState.isVisible){
		coroutineScope.launch {
			modalSheetState.hide()
			mapViewModel.hideSheet()
		}
	}
//	}

	//val modifier = Modifier.height(570.dp)
	val modifier = Modifier.height((LocalConfiguration.current.screenHeightDp*0.85).dp)

	ModalBottomSheetLayout(
		modifier = Modifier.fillMaxHeight(),
		sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
		sheetState = modalSheetState,
		sheetContent = {
			IconButton(
				onClick = {
					coroutineScope.launch {
						modalSheetState.hide()
						mapViewModel.hideSheet()
					}
				}
			) {
				androidx.compose.material.Icon(
					Icons.Default.Close,
					contentDescription = null, //endre
					tint = MaterialTheme.colors.onSurface
				)
			}
			Column(
				modifier =  modifier
					//.fillMaxWidth()
					.padding(top = 0.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)

			) {
				if (screenUiState.value.showSheet == Sheet.Rules) {
					//Spacer(modifier = Modifier.height(16.dp))
					//Tekst regler her:
					val modifier = Modifier
					RulePage(modifier)
//					coroutineScope.launch {
//						modalSheetState.show()
//					}

				} else if (screenUiState.value.showSheet == Sheet.Weather) {

					WeatherPage(sunWeatherUiState, context, screenUiState.value.selectedLocation!!)
					println("SHOWING WEATHER PAGE for location ${screenUiState.value.selectedLocation}")
					println("weathermodel: " + sunWeatherUiState.value.currentWeather)

//					coroutineScope.launch {
//							modalSheetState.show()
////							modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
//					}
				}
				else if (screenUiState.value.showSheet == Sheet.Feedback) {
					println("SHOWING FEEDBACK PAGE for location ${screenUiState.value.selectedLocation}")

					FeedbackPage(mapViewModel)
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
                //contentAlignment = Alignment.Center,
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
//					println("Marker lokasjon: " + mapViewModel.markerLocation.toString())
//					println("showMarker: " + mapViewModel.showMarker)
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
//							mapViewModel.selectLocation(mapViewModel.markerLocation)
							println("CLICKED INFO WINDOW")
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
						state = rememberMarkerState(position = LatLng(latCoordinates[i], lngCoordinates[i])),
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
						.fillMaxWidth(),
					color = Color.White,
					shape = RoundedCornerShape(8.dp)
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



data class AutocompleteResult(
	val address: String,
	val placeId: String,
)


@Composable
fun SearchBar(mapViewModel: MapViewModel){

	val focusManager = LocalFocusManager.current

//svaret er lagret her

	Column(
		modifier = Modifier.padding(5.dp),
		horizontalAlignment = Alignment.CenterHorizontally) {

		Row(modifier = Modifier.fillMaxWidth()) {
			androidx.compose.material.OutlinedTextField(
				value = mapViewModel.text,
				onValueChange = {
					mapViewModel.text = it
					mapViewModel.searchPlaces(it)
					println(mapViewModel.locationAutofill.toString())
				},
				placeholder = { androidx.compose.material.Text("Search") },
				shape = RoundedCornerShape(16.dp),
				leadingIcon = {
					androidx.compose.material.Icon(
						Icons.Filled.Search,
						contentDescription = null
					)

				},
				trailingIcon = {
					androidx.compose.material.Icon(
						imageVector = Icons.Filled.Close,
						contentDescription = null,
						modifier = Modifier.clickable {
							if(mapViewModel.text.isEmpty()) {
								focusManager.clearFocus()
							}
							else {
								mapViewModel.text = ""
								mapViewModel.locationAutofill.clear()
							}
						}
					)
				} ,
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Search
				),
				keyboardActions = KeyboardActions(
					onDone = { focusManager.clearFocus() }
				),
				singleLine = true,
				textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
				colors = TextFieldDefaults.outlinedTextFieldColors(
					backgroundColor = Color.White,
					cursorColor = Color.Black,
					focusedBorderColor = Color.Transparent,
					unfocusedBorderColor = Color.Transparent
				),
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp, vertical = 8.dp)
			)
		}


		AnimatedVisibility(
			mapViewModel.locationAutofill.isNotEmpty(),
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
		) {
			LazyColumn(
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				items(mapViewModel.locationAutofill) {
					Row(modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp)
						.clickable {
							mapViewModel.text = it.address
							mapViewModel.locationAutofill.clear()
							mapViewModel.getCoordinates(it)
							//println("showMarker får ny verdi: " + mapViewModel.showMarker.value)
						}) {
						Text(text = it.address)
					}
				}
			}
			Spacer(Modifier.height(16.dp))
		}
	}
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationBar(modifier: Modifier = Modifier,
				  containerColor: Color = NavigationBarDefaults.containerColor,
				  tonalElevation: Dp = NavigationBarDefaults.Elevation,
				  windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
				  mapViewModel: MapViewModel,
				  sunWeatherUiState: State<SunWeatherUiState>,
				  context: Context,
				  userLocation: LatLng,
				  modalSheetState: ModalBottomSheetState,
				  coroutineScope: CoroutineScope,
) {

	var selectedItem by remember { mutableStateOf("") }
	val items = listOf("Search", "Feedback", "Weather", "Rules")


	BottomAppBar {
		Row {
			NavigationBarItem(
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_search_96), contentDescription = items[0]) },
				//label = { Text("Search") },
				selected = selectedItem == items[0],
				onClick = {
					selectedItem = if(selectedItem != items[0]) {
						items[0]
					} else {
						""
					}
					mapViewModel.toggleShowSearchBar() }
			)
			NavigationBarItem(
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_pass_fail_96), contentDescription = items[1]) },
				//label = { Text("Search") },
				selected =
				selectedItem == items[1],
				onClick = {
					mapViewModel.showSheet(Sheet.Feedback)
					coroutineScope.launch {
						modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
					}
				}
			)
			NavigationBarItem(
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_sun_96), contentDescription = items[2]) },
				//label = { Text("Weather") },
				selected = selectedItem == items[2],
				onClick = {

					mapViewModel.selectLocation(userLocation)
					mapViewModel.showSheet(Sheet.Weather)
					coroutineScope.launch {
						modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
					}
				}
			)
			NavigationBarItem(
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_literature_96), contentDescription = items[3]) },
				//label = { Text("Rules") },
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

