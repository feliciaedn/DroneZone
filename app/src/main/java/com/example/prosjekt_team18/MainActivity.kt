package com.example.prosjekt_team18

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.prosjekt_team18.data.maps.LocationDetails
import com.example.prosjekt_team18.data.sunrise.SunDataSource
import com.example.prosjekt_team18.data.weather.WeatherDataSource
import com.example.prosjekt_team18.ui.screens.MainScreen
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

class MainActivity : ComponentActivity() {

	private val weatherDataSource = WeatherDataSource()
	private val sunDataSource = SunDataSource()

	private val mapViewModel: MapViewModel = MapViewModel(weatherDataSource, sunDataSource)

	private var fusedLocationClient: FusedLocationProviderClient? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			//lokasjonsknappen er trykket ned
			var buttonClicked by remember {
				mutableStateOf(isPermissionGranted())
			}

			var permissionGranted by remember {
				mutableStateOf(isPermissionGranted())
			}

			val permissionLauncher =
				rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionGranted_ ->
					// this is called when the user selects allow or deny
					Toast.makeText(
						this@MainActivity,
						"permissionGranted_ $permissionGranted_",
						Toast.LENGTH_SHORT
					).show()
					permissionGranted = permissionGranted_
				}

			if (!permissionGranted) {
				Button(modifier = Modifier
					.fillMaxSize()
					.wrapContentSize(Alignment.Center),
					enabled = !permissionGranted, // if the permission is NOT granted, enable the button
					onClick = {
						if (!permissionGranted) {
							// ask for permission
							permissionLauncher.launch(ACCESS_FINE_LOCATION)
							buttonClicked = true
						}
					}) {
					Text(text = if (permissionGranted) "Permission Granted" else "Enable Permission")
				}
			}

			if (permissionGranted) {
				// update your UI
				Toast.makeText(
					this@MainActivity,
					"Permission granted",
					Toast.LENGTH_SHORT
				).show()
			}

			fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

			//var default by remember { mutableStateOf(LatLng(59.9, 10.75)) } // oslo-koordinater
			var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

			if (ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACCESS_FINE_LOCATION
				) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACCESS_COARSE_LOCATION
				) != PackageManager.PERMISSION_GRANTED
			) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				println("går inn2")
			} else {
				println("går inn3")
				fusedLocationClient!!.lastLocation.addOnSuccessListener { _location: Location? ->
					if (_location != null) {
						userLocation = LatLng(_location.latitude, _location.longitude)
						mapViewModel.mapUiState.value.currentLocation =
							LocationDetails(_location.latitude, _location.longitude)
						mapViewModel.mapUiState.value.properties =
							MapProperties(isMyLocationEnabled = true, mapType = MapType.TERRAIN)

						println("inni if")
						println(userLocation.toString())
					}
				}
			}

			var cameraPositionState: CameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(userLocation, 14f))


			if(buttonClicked) {
				MainScreen(mapViewModel, cameraPositionState, userLocation, permissionGranted, this)
			}
		}
	}

	// check initially if the permission is granted
	private fun isPermissionGranted(): Boolean {
		return ContextCompat.checkSelfPermission(
			this,
			ACCESS_FINE_LOCATION
		) == PackageManager.PERMISSION_GRANTED
	}
}
