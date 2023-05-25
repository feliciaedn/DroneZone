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
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.prosjekt_team18.data.FeedbackCheck
import com.example.prosjekt_team18.data.maps.LocationDetails
import com.example.prosjekt_team18.data.sunrise.SunDataSource
import com.example.prosjekt_team18.data.weather.WeatherDataSource
import com.example.prosjekt_team18.ui.screens.MainScreen
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
//	private lateinit var fusedLocationClient: FusedLocationProviderClient
//	override fun onSaveInstanceState(outState: Bundle) {
//		outState.run {
//			put
//			(GAME_STATE_KEY, gameState)
//			putString(TEXT_VIEW_KEY, textView.text.toString())
//		}
//		// Call superclass to save any view hierarchy.
//		super.onSaveInstanceState(outState)
//	}
	private val mapViewModel: MapViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//			val mapViewModel: MapViewModel by remember {mutableStateOf(MapViewModel(WeatherDataSource(), SunDataSource(), FeedbackCheck()))}

			mapViewModel.fusedLocationClient =
				LocationServices.getFusedLocationProviderClient(this)
			Places.initialize(this.applicationContext, BuildConfig.GOOGLE_API_KEY)
			mapViewModel.placesClient = Places.createClient(this)

			//lokasjonsknappen er trykket ned
			var buttonClicked by remember {
				mutableStateOf(isPermissionGranted())
			}

			var startup by remember { mutableStateOf(false) }

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

			if (!permissionGranted && !startup) {
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

			if (permissionGranted && !startup) {
				// update your UI
				Toast.makeText(
					this@MainActivity,
					"Permission granted",
					Toast.LENGTH_SHORT
				).show()
				startup = true
			}

			//fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

			//var default by remember { mutableStateOf(LatLng(59.9, 10.75)) } // oslo-koordinater
			//var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

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
				/*else {
					println("går inn3")
					mapViewModel.fusedLocationClient.lastLocation.addOnSuccessListener { _location: Location? ->
						if (_location != null) {
							mapViewModel.userLocation = LatLng(_location.latitude, _location.longitude)
							mapViewModel.mapUiState.value.currentLocation =
								LocationDetails(_location.latitude, _location.longitude)
							mapViewModel.mapUiState.value.properties =
								MapProperties(isMyLocationEnabled = true, mapType = MapType.TERRAIN)
							mapViewModel.hasLocation = true
							println("inni if")
							println(mapViewModel.userLocation.toString())
						}
					}*/
				// Request current location
				mapViewModel.fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { _location: Location? ->
					println("hentet brukers lokasjon")
					if (_location == null) {
						Toast.makeText(
							this@MainActivity,
							"Error: Kunne ikke laste inn nåværende lokasjon",
							Toast.LENGTH_SHORT
						).show()

					} else {
						mapViewModel.userLocation = LatLng(_location.latitude, _location.longitude)
						mapViewModel.mapUiState.value.currentLocation =
							LocationDetails(_location.latitude, _location.longitude)
						mapViewModel.mapUiState.value.properties =
							MapProperties(isMyLocationEnabled = true, mapType = MapType.TERRAIN)
						mapViewModel.hasLocation = true
						println("inni else2")
						println(mapViewModel.userLocation.toString())
					}
				}
			}

			//var cameraPositionState: CameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(userLocation, 14f))
			val cameraPositionState = rememberCameraPositionState {
				position = CameraPosition.fromLatLngZoom(mapViewModel.userLocation, 15f)
			}

			if(buttonClicked) {
				println("PERMISSION GRANTED: ")
				mapViewModel.selectLocation(mapViewModel.userLocation)
				MainScreen(mapViewModel, cameraPositionState, mapViewModel.userLocation, permissionGranted, this)
			}

			LaunchedEffect(mapViewModel.hasLocation) {
				if(!mapViewModel.hasLaunched && mapViewModel.userLocation != LatLng(0.0, 0.0)) {
					println("inni launchedeffect..")
					cameraPositionState.animate(CameraUpdateFactory.newLatLng(mapViewModel.userLocation))
					mapViewModel.hasLaunched = true
				}
			}

			if(mapViewModel.searchLatLong != LatLng(0.0, 0.0)) {
				LaunchedEffect(mapViewModel.searchLatLong) {
					cameraPositionState.animate(CameraUpdateFactory.newLatLng(mapViewModel.searchLatLong))
				}
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



//	override fun onSaveInstanceState(savedInstanceState: Bundle) {
//		super.onSaveInstanceState(savedInstanceState)
//		// Save UI state changes to the savedInstanceState.
//		// This bundle will be passed to onCreate if the process is
//		// killed and restarted.
//		savedInstanceState.putBoolean("MyBoolean", true)
//		savedInstanceState.putDouble("myDouble", 1.9)
//		savedInstanceState.putInt("MyInt", 1)
//		savedInstanceState.putString("MyString", "Welcome back to Android")
//		// etc.
//	}
}
