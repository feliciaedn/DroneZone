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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.MainActivity
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.example.prosjekt_team18.ui.viewmodels.WeatherUiState
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*



@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MapScreen(mapViewModel: MapViewModel, cameraPositionState: CameraPositionState, userLocation: LatLng, permissionGranted: Boolean, context: Context) {

	val positionUiState by mapViewModel.mapUiState.collectAsState()
	val screenUiState by mapViewModel.screenUiState.collectAsState()
	val weatherUiState by mapViewModel.weatherUiState.collectAsState()



	Column(modifier = Modifier.fillMaxSize()) {


		Box(modifier = Modifier.weight(15f)) {
			//Displayer google mappet
			GoogleMap(
				properties = positionUiState.properties,
				cameraPositionState = cameraPositionState
			)
			if (screenUiState.showSearchBar) {
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
//			if (screenUiState.showWeather) {
//				WeatherMessage(weatherUiState)
//				weatherModel.
//			}

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


		NavigationBar(Modifier.padding(12.dp),  NavigationBarDefaults.containerColor,12.dp,NavigationBarDefaults.windowInsets, mapViewModel, weatherUiState, context, userLocation)
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
	var input by remember {
		mutableStateOf("")
	}

	Column(
		modifier = Modifier.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally) {

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
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Text,
				imeAction = ImeAction.Search
			),
			keyboardActions = KeyboardActions(
				onSearch = {
					if (input.isNotEmpty()) {
						println("hfbejhf 6666")
						//onSearch(input)
					}
					focusManager.clearFocus()
					input = ""

				}
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
							
							//currentCameraPositionState.position = CameraPosition(mapViewModel.currentLatLong)
						}) {
						Text(it.address)
					}
				}
			}
			Spacer(Modifier.height(16.dp))
		}
	}


}
@Composable
fun NavigationBar(modifier: Modifier = Modifier, 
				  containerColor: Color = NavigationBarDefaults.containerColor, 
				  tonalElevation: Dp = NavigationBarDefaults.Elevation, 
				  windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
				  mapViewModel: MapViewModel,
				  weatherUiState: WeatherUiState,
				  context: Context,
				  userLocation: LatLng) {
	var selectedItem by remember { mutableStateOf("") }
	val items = listOf("Search", "Map", "Weather", "Rules")


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
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_map_marker_96_1), contentDescription = items[1]) },
				//label = { Text("Search") },
				selected =
				selectedItem == items[1],
				onClick = { /* TO DO */ }
			)
			NavigationBarItem(                
				icon = { 
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_sun_96), contentDescription = items[2]) },
				//label = { Text("Weather") },
				selected = selectedItem == items[2],
				onClick = {

					mapViewModel.updateWeatherData(userLocation)
					val weatherModel = weatherUiState.currentWeather
//					val contextForToast = LocalContext.current.applicationContext

					if (weatherModel != null) {
						Toast.makeText(
							context,
							"Temp: ${weatherModel.temperature}°C, Vind: ${weatherModel.windSpeed} ${weatherModel.windSpeedUnit}, VindVei: ${weatherModel.windDirection}°, Rain: ${weatherModel.rainNext6h} ${weatherModel.rainUnit}",
							Toast.LENGTH_LONG
						).show()
					}

//					mapViewModel.updateWeatherData(userLocation)
//					mapViewModel.showWeather()
				}
			)
			NavigationBarItem(                
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_list_view_96), contentDescription = items[3]) },
				//label = { Text("Rules") },
				selected = selectedItem == items[3],
				onClick = { /* TO DO */ }
			)        
		}    
	}
}
//@Composable
//fun WeatherMessage(weatherUiState: WeatherUiState) {

//	} else {
//		Toast.makeText(
//			contextForToast,
//			"ERROR: Kunne ikke laste værdata",
//			Toast.LENGTH_SHORT
//		).show()
//	}

//}