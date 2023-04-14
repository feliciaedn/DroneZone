package com.example.prosjekt_team18.ui.screens


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.example.prosjekt_team18.ui.viewmodels.ScreenUiState
import com.example.prosjekt_team18.ui.viewmodels.Sheet
import com.example.prosjekt_team18.ui.viewmodels.WeatherUiState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition",
	"UnusedMaterialScaffoldPaddingParameter"
)
@Composable
fun MainScreen(mapViewModel: MapViewModel, cameraPositionState: CameraPositionState, userLocation: LatLng, permissionGranted: Boolean, context: Context) {
	val screenUiState = mapViewModel.screenUiState.collectAsState()
	val coroutineScope = rememberCoroutineScope()

	val modalSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		confirmStateChange = { it != ModalBottomSheetValue.Expanded },
		skipHalfExpanded = true,
	)

	if(screenUiState.value.showSheet != Sheet.None){
		coroutineScope.launch {
			if (modalSheetState.isVisible){
				modalSheetState.hide()
			} else {
				modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
			}
		}
	} else {
		if (modalSheetState.isVisible){
			coroutineScope.launch {
				modalSheetState.hide()
			}
		}
	}

	val modifier = Modifier.height(570.dp)

	ModalBottomSheetLayout(
		modifier = Modifier.fillMaxHeight(),
		sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
		sheetState = modalSheetState,
		sheetContent = {
			IconButton(
				onClick = {
					coroutineScope.launch { modalSheetState.hide() }
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
					.padding(16.dp)

			) {
				if (screenUiState.value.showSheet != Sheet.None && screenUiState.value.showSheet == Sheet.Rules) {
					//Spacer(modifier = Modifier.height(16.dp))
					//Tekst regler her:
					var modifier = Modifier
					RulePage(modifier)

				} else if (screenUiState.value.showSheet != Sheet.None && screenUiState.value.showSheet == Sheet.Weather) {

					WeatherPage()

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
					MapScreen(mapViewModel, cameraPositionState, userLocation, permissionGranted, context, screenUiState)

				}
            }
        }



	}


}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MapScreen(mapViewModel: MapViewModel,
			  cameraPositionState: CameraPositionState,
			  userLocation: LatLng,
			  permissionGranted: Boolean,
			  context: Context,
			  screenUiState: State<ScreenUiState>
) {

	val positionUiState by mapViewModel.mapUiState.collectAsState()
	val weatherUiState by mapViewModel.weatherUiState.collectAsState()



	Column(modifier = Modifier.fillMaxSize()) {


		Box(modifier = Modifier.weight(15f)) {
			//Displayer google mappet
			GoogleMap(
				properties = positionUiState.properties,
				cameraPositionState = cameraPositionState
			)
			if (screenUiState.value.showSearchBar) {
				SearchBar()
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
@Composable
fun WeatherPage(){
	Column( modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally){
		Text("Oslo, Norway", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 50.sp,color = Color(0xFF1B467C)))
		Image (painter = painterResource(id = R.drawable._11721_cloud_icon), contentDescription = null, modifier = Modifier.size(160.dp))
		Text("12°", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 60.sp,color = Color.Black))
		Text("Overskyet", style = TextStyle( fontSize = 17.sp,color = Color(0xFF1B467C)))
		Column( modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally){
			WeatherCard()
			SunCard()
			//hei

		}


	}
}

@Composable
fun WeatherCard(
) {
	Card (
		shape = RoundedCornerShape(10.dp),
		backgroundColor = Color.White,
		modifier = Modifier
			.padding(horizontal = 16.dp, vertical=0.dp)
			.fillMaxWidth()
			.width(120.dp)
			.height(88.dp)
			// Merger alle elementer i carden for bedre tilgjenglighet
			.semantics(mergeDescendants = true) {},

		elevation = 1.dp
	) {

		Column(
			horizontalAlignment = Alignment.Start,
			modifier = Modifier
				.padding(10.dp)
				.fillMaxSize()
		) {
			Column() {
				Image (painter = painterResource(id = R.drawable._72922), contentDescription = null, modifier = Modifier.size(40.dp))
				Text("10 m/s", style = TextStyle( fontSize = 10.sp,color = Color.Black))
				Text("Vind", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))

			}

		}
		Column(
			horizontalAlignment = Alignment.End,
			modifier = Modifier
				.padding(10.dp)
				.fillMaxSize()
		) {
			Column(){
				Image (painter = painterResource(id = R.drawable._038403), contentDescription = null, modifier = Modifier.size(40.dp))
				Text("0.6", style = TextStyle( fontSize = 10.sp,color = Color.Black))
				Text("Regn", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))
			}

		}




	}
}

@Composable
fun SunCard(

) {
	Card (
		shape = RoundedCornerShape(10.dp),
		backgroundColor = Color.White,
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth()
			.width(120.dp)
			.height(88.dp)
			// Merger alle elementer i carden for bedre tilgjenglighet
			.semantics(mergeDescendants = true) {},

		elevation = 1.dp
	) {

		Column(
			horizontalAlignment = Alignment.Start,
			modifier = Modifier
				.padding(16.dp)
				.fillMaxSize()
		) {
			Image (painter = painterResource(id = R.drawable.long_arrow_up), contentDescription = null, modifier = Modifier.size(25.dp))
			Text("06:08", style = TextStyle( fontSize = 10.sp,color = Color.Black))
			Text("Soloppgang", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))
		}
		Column(
			horizontalAlignment = Alignment.End,
			modifier = Modifier
				.padding(16.dp)
				.fillMaxSize()
		) {
			Column( ){
				Image (painter = painterResource(id = R.drawable._247262), contentDescription = null, modifier = Modifier.size(25.dp))
				Text("20:27", style = TextStyle( fontSize = 10.sp,color = Color.Black))
				Text("Solnedgang", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))

			}

		}




	}
}

@Composable
fun SearchBar(){

	val focusManager = LocalFocusManager.current

//svaret er lagret her
	var input by remember {
		mutableStateOf("")
	}
	androidx.compose.material.OutlinedTextField(
		value = input,
		onValueChange = { input = it },
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
	var selectedItem by remember { mutableStateOf(0) }
	val items = listOf("Search", "Map", "Weather", "Rules")

	BottomAppBar {
		Row {
			NavigationBarItem(
				icon = {
					//Image(imageVector = ImageVector.vectorResource(id = R.drawable.icons8_search_24), contentDescription = "Song") },
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_search_96), contentDescription = items[0]) },
				//label = { Text("Search") },
				selected = selectedItem == 0,
				onClick = { mapViewModel.toggleShowSearchBar() }
			)
			NavigationBarItem(
				icon = {
					//Image(imageVector = ImageVector.vectorResource(id = R.drawable.icons8_search_24), contentDescription = "Song") },
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_map_marker_96_1), contentDescription = items[1]) },
				//label = { Text("Search") },
				selected = selectedItem == 1,
				onClick = { /* TO DO */ }
			)
			NavigationBarItem(
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_sun_96), contentDescription = items[2]) },
				//label = { Text("Weather") },
				selected = selectedItem == 2,
				onClick = {
//					mapViewModel.updateWeatherData(userLocation)
//					val weatherModel = weatherUiState.currentWeather
////					val contextForToast = LocalContext.current.applicationContext
//
//					if (weatherModel != null) {
//						Toast.makeText(
//							context,
//							"Temp: ${weatherModel.temperature} grader ${weatherModel.tempUnit}, Vind: ${weatherModel.windSpeed} ${weatherModel.windSpeedUnit}, VindVei: ${weatherModel.windDirection} grader, Rain: ${weatherModel.rainNext6h} ${weatherModel.rainUnit}",
//							Toast.LENGTH_LONG
//						).show()
//					}
					mapViewModel.toggleShowSheet(Sheet.Weather)

//					mapViewModel.updateWeatherData(userLocation)
//					mapViewModel.showWeather()
				}
			)
			NavigationBarItem(
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_list_view_96), contentDescription = items[3]) },
				//label = { Text("Rules") },
				selected = selectedItem == 3,
				onClick = {mapViewModel.toggleShowSheet(Sheet.Rules)}
			)
		}
	}
}
@Composable
fun RulePage(modifier: Modifier = Modifier) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Regler for å fly drone",
			modifier = modifier.padding(16.dp),
			style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp,color = Color(0xFF1B467C)))
		RuleImageColumn(modifier)
	}
}
@Composable
fun RuleImageColumn(modifier: Modifier = Modifier) {
	val ruleImageIds = listOf(
		R.drawable.regel1_registrering,
		R.drawable.regel2_forsikring,
		R.drawable.regel3_droneforbudssoner,
		R.drawable.regel4_sedronen,
		R.drawable.regel5_ikkeflyover,
		R.drawable.regel6_avstand
	)

	val ruleDescriptionIds = stringArrayResource(R.array.rule_descriptions)

	LazyColumn(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		var ruleNr = 1
		items(ruleImageIds) { ruleImageId ->
			RuleCard(ruleImageId, ruleDescriptionIds[ruleNr++ % ruleDescriptionIds.size], modifier)
			//RuleCard(ruleImageId, ruleDescriptionIds[ruleNr++], modifier)
		}
	}
}

@Composable
fun RuleCard(
	imageId: Int,
	ruleDescription: String,
	modifier: Modifier = Modifier,
) {
	Card (
		shape = RoundedCornerShape(10.dp),
		colors =  CardDefaults.cardColors(
			containerColor =  Color.White,
		),
		modifier = modifier
			.padding(16.dp)
			.fillMaxWidth()
			// Merger alle elementer i carden for bedre tilgjenglighet
			.semantics(mergeDescendants = true) {},

		elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.padding(16.dp)
				.fillMaxSize()

		) {

			Image (
				painter = painterResource(id = imageId),
				contentDescription = ruleDescription
			)

		}

	}
}

@Preview(showBackground = true)
@Composable
fun RuleColumnPreview() {
	RulePage()
}
