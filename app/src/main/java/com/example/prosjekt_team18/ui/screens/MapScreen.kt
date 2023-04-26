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
import com.example.prosjekt_team18.ui.viewmodels.WeatherUiState
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.fragment.app.FragmentManager
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

	if(screenUiState.value.showSheet != Sheet.None){
		coroutineScope.launch {
			modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
		}
	} else {
		if (modalSheetState.isVisible){
			coroutineScope.launch {
				modalSheetState.hide()
				mapViewModel.hideSheet()
			}
		}
	}

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
					var modifier = Modifier
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
						modalSheetState)

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
) {

	val positionUiState by mapViewModel.mapUiState.collectAsState()

	var lagre2 = remember { mutableStateOf("Trykk for å sjekke værmelding") }
	val latCoordinates = listOf(
		(69.9779), (69.3106), (58.5159), (69.0595), (60.2918), (70.8700), (67.2683), (65.4599),
		(70.6028), (61.5858), (61.3911), (70.6800), (68.4919), (70.4866), (59.3445), (71.0087),
		(60.2120), (69.7214), (59.9693), (58.2038), (63.1139), (70.0664), (68.1535), (71.0275),
		(66.3646), (62.7464), (65.7844), (64.4725), (59.5652), (78.9278), (62.6512), (60.1976),
		(59.3976), (62.5770), (64.8392), (67.5272), (61.8290), (59.1824), (65.9594), (59.1843),
		(61.1575), (58.8804), (68.5795), (59.7930), (78.2469), (68.2450), (69.7856), (69.6819),
		(63.4583), (70.0641), (70.3564), (63.6953), (62.1800), (62.5585)
	)
	val lngCoordinates = listOf(
		(23.3466), (16.1239), (8.7027), (18.5378), (5.2220), (29.0303), (14.3622), (12.2092),
		(29.6923), (5.0242), (5.7642), (23.6755), (16.6841), (22.1471), (5.2166), (25.9775),
		(10.3180), (29.8830), (11.0395), (8.0838), (7.8254), (24.9819), (13.6145), (27.8291),
		(14.3034), (7.2608), (13.2170), (11.5717), (9.2183), (11.8749), (9.8504), (11.1004),
		(11.3468), (11.3530), (11.1443), (12.1058), (6.1071), (10.2569), (12.4755), (9.5699),
		(7.1374), (5.6314), (15.0315), (5.3424), (15.4933), (14.6668), (20.9576), (18.9163),
		(10.9226), (29.8385), (31.0397), (9.6035), (6.0795), (6.1153)
	)
	val airportNames = listOf(
		("Alta lufthavn"), ("Andøya lufthavn"), ("Arendal lufthavn"), ("Bardufoss lufthavn"),
		("Bergen lufthavn"), ("Berlevåg lufthavn"), ("Bodø lufthavn"), ("Brønnøysund lufthavn, Brønnøy"),
		("Båtsfjord lufthavn"), ("Florø lufthamn"), ("Førde lufthamn, Bringeland"), ("Hammerfest lufthavn"),
		("Harstad/Narvik lufthavn, Evenes"), ("Hasvik lufthavn"), ("Haugesund lufthavn, Karmøy"),
		("Honningsvåg lufthavn, Valan"), ("Hønefoss flyplass, Eggemoen"), ("Kirkenes lufthavn, Høybuktmoen"),
		("Kjeller flyplass"), ("Kristiansand lufthavn, Kjevik"), ("Kristiansund lufthavn, Kvernberget"),
		("Lakselv lufthavn, Banak"), ("Leknes lufthavn"), ("Mehamn lufthavn"), ("Mo i Rana lufthavn, Røssvoll"),
		("Molde lufthavn, Årø"), ("Mosjøen lufthavn, Kjærstad"), ("Namsos lufthavn"), ("Notodden lufthavn"),
		("Ny-Ålesund flyplass, Havnerabben"), ("Oppdal flyplass, Fagerhaug"), ("Oslo lufthavn, Gardermoen"),
		("Rakkestad flyplass, Åstorp"), ("Røros lufthavn"), ("Rørvik lufthavn, Ryum"),
		("Røst lufthavn"), ("Sandane lufthamn, Anda"), ("Sandefjord lufthavn, Torp"),
		("Sandnessjøen lufthavn, Stokka"), ("Skien flyplass, Geiteryggen"), ("Sogndal lufthavn, Haukåsen"),
		("Stavanger lufthavn, Sola"), ("Stokmarknes lufthavn, Skagen"), ("Stord lufthamn, Sørstokken"),
		("Svalbard lufthavn, Longyear"), ("Svolvær lufthavn, Helle"), ("Sørkjosen lufthavn"),
		("Tromsø lufthavn, Langnes"), ("Trondheim lufthavn, Værnes"), ("Vadsø lufthavn"),
		("Vardø lufthavn, Svartnes"), ("Ørland lufthavn"), ("Ørsta/Volda lufthamn, Hovden"),
		("Ålesund lufthavn, Vigra")
	)

	Column(modifier = Modifier.fillMaxSize()) {


		Box(modifier = Modifier.weight(15f)) {
			//Displayer google mappet
			GoogleMap(
				properties = positionUiState.properties,
				cameraPositionState = cameraPositionState,
				onMapClick = {
					if(mapViewModel.showMarker.value) {
						println("HELLOOOOO-------")
						lagre2.value = "Trykk for å sjekke værmelding"
					}
					mapViewModel.showMarker.value = !mapViewModel.showMarker.value
					mapViewModel.markerLocation = it
					println("Marker lokasjon: " + mapViewModel.markerLocation.toString())
					println("showMarker: " + mapViewModel.showMarker)
				}
			) {
				if(mapViewModel.showMarker.value) {
					val markerState = rememberMarkerState(position = mapViewModel.markerLocation)
					markerState.showInfoWindow()
					Marker(
						state = markerState,
						title = "Lokasjon",
						snippet = lagre2.value,
						icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
						onInfoWindowClick = {
							mapViewModel.selectLocation(mapViewModel.markerLocation)
							mapViewModel.showSheet(Sheet.Weather)

						}
					)

				}

				for (i in latCoordinates.indices) {
					Marker(
						state = rememberMarkerState(position = LatLng(latCoordinates[i], lngCoordinates[i])),
						title = airportNames[i],
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

				/*
				// Get a reference to the SupportMapFragment
				val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

				// Get the ID of the SupportMapFragment
				val mapFragmentId = mapFragment.id

				// Obtain a reference to the GoogleMap object
				mapFragment.getMapAsync { googleMap ->

					// Define the circle options
					val circleOptions = CircleOptions()
						.center(LatLng(37.4, -122.1))
						.radius(5000.0)

					// Add the circle to the map
					val circle = googleMap.addCircle(circleOptions)
				}

				 */



				/*
				val googleMap = GoogleMap()

				val circleOptions = CircleOptions()
					.center(LatLng(37.4, -122.1))
					.radius(5000.0) // In meters


				val circle = googleMap.addCircle(circleOptions)

				 */
				//val circle = mapView.getMap().addCircle(circleOptions)


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
		)
	}

	}



data class AutocompleteResult(
	val address: String,
	val placeId: String,
)
}

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
				  modalSheetState: ModalBottomSheetState
) {
	val coroutineScope = rememberCoroutineScope()

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

