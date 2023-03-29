package com.example.prosjekt_team18.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.google.maps.android.compose.*


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MapScreen(mapViewModel: MapViewModel, cameraPositionState: CameraPositionState) {

	val positionUiState by mapViewModel.mapUiState.collectAsState()

	Column(modifier = Modifier.fillMaxSize()) {


		Box(modifier = Modifier.weight(15f)) {
			//Displayer google mappet
			GoogleMap(
				properties = positionUiState.properties,
				cameraPositionState = cameraPositionState
			)
		}

		NavigationBar(Modifier.padding(12.dp),  NavigationBarDefaults.containerColor,12.dp,NavigationBarDefaults.windowInsets)
		}
	}

@Composable
fun NavigationBar(modifier: Modifier = Modifier, 
				  containerColor: Color = NavigationBarDefaults.containerColor, 
				  tonalElevation: Dp = NavigationBarDefaults.Elevation, 
				  windowInsets: WindowInsets = NavigationBarDefaults.windowInsets) {    
	var selectedItem by remember { mutableStateOf(0) }    
	val items = listOf("Search", "Map", "Weather", "Rules")
	BottomAppBar {        
		Row {
			NavigationBarItem(
				icon = {
					//Image(imageVector = ImageVector.vectorResource(id = R.drawable.icons8_search_24), contentDescription = "Song") },
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_search_24), contentDescription = items[0]) },
				//label = { Text("Search") },
				selected = selectedItem == 1,
				onClick = { /* TO DO */ }
			)
			NavigationBarItem(
				icon = {
					//Image(imageVector = ImageVector.vectorResource(id = R.drawable.icons8_search_24), contentDescription = "Song") },
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_map_marker_24), contentDescription = items[1]) },
				//label = { Text("Search") },
				selected = selectedItem == 1,
				onClick = { /* TO DO */ }
			)
			NavigationBarItem(                
				icon = { 
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_weather_33), contentDescription = items[2]) },
				//label = { Text("Weather") },
				selected = selectedItem == 2,                
				onClick = { /* TO DO */ }
			)
			NavigationBarItem(                
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_rules_32), contentDescription = items[3]) },
				//label = { Text("Rules") },
				selected = selectedItem == 3,                
				onClick = { /* TO DO */ }
			)        
		}    
	}
}