package com.example.prosjekt_team18.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.MainActivity
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MapScreen(mapViewModel: MapViewModel, cameraPositionState: CameraPositionState, userLocation: LatLng ,permissionGranted: Boolean, context: Context) {

	val positionUiState by mapViewModel.mapUiState.collectAsState()

	Column(modifier = Modifier.fillMaxSize()) {


		Box(modifier = Modifier.weight(15f)) {
			//Displayer google mappet
			GoogleMap(
				properties = positionUiState.properties,
				cameraPositionState = cameraPositionState
			)
			if (userLocation == LatLng(0.0, 0.0)) {
				IconButton(
					modifier = Modifier
						.fillMaxWidth()
						.wrapContentWidth(Alignment.End)
						.padding(top = 7.dp, end = 7.dp).shadow(20.dp),
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
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_search_96), contentDescription = items[0]) },
				//label = { Text("Search") },
				selected = selectedItem == 1,
				onClick = { /* TO DO */ }
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
				onClick = { /* TO DO */ }
			)
			NavigationBarItem(                
				icon = {
					Image(modifier = Modifier.size(32.dp) ,painter = painterResource(id = R.drawable.icons8_list_view_96), contentDescription = items[3]) },
				//label = { Text("Rules") },
				selected = selectedItem == 3,                
				onClick = { /* TO DO */ }
			)        
		}    
	}
}