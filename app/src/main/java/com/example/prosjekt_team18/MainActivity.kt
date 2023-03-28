package com.example.prosjekt_team18

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.prosjekt_team18.ui.presentation.LocationDetails
import com.example.prosjekt_team18.ui.presentation.MapScreen
import com.example.prosjekt_team18.ui.presentation.MapViewModel
import com.example.prosjekt_team18.ui.theme.Prosjekt_team18Theme
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {

	private val mapViewModel: MapViewModel by viewModels()

	private var locationCallback: LocationCallback? = null
	private var fusedLocationClient: FusedLocationProviderClient? = null

	private val timeInterval: Long = 10000 //oppdateringsfrekvens av lokasjon i millisekunder, 120000 = hvert 2. minutt
	private val minimalDistance: Float = 50.0F

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

			/*
			locationCallback = object : LocationCallback() {
				override fun onLocationResult(p0: LocationResult) {
					for(lo in p0.locations) {
						mapViewModel.mapUiState.value.currentLocation = LocationDetails(lo.latitude, lo.longitude)
						println("HER ER MAIN: Latitude: " + mapViewModel.mapUiState.value.currentLocation.latitude + ". Longitude: +" + mapViewModel.mapUiState.value.currentLocation.longitude)
					}
				}
			}
			 */

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

			Button(
				enabled = !permissionGranted, // if the permission is NOT granted, enable the button
				onClick = {
					if (!permissionGranted) {
						// ask for permission
						permissionLauncher.launch(ACCESS_FINE_LOCATION)
					}
				}) {
				Text(text = if (permissionGranted) "Permission Granted" else "Enable Permission")
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
			}
			println("går inn3")
			fusedLocationClient!!.lastLocation.addOnSuccessListener { _location : Location? ->
				if (_location != null) {
					userLocation = LatLng(_location.latitude, _location.longitude)
					mapViewModel.mapUiState.value.currentLocation = LocationDetails(_location.latitude, _location.longitude)
					mapViewModel.mapUiState.value.properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.TERRAIN)
					println("inni if")
					println(userLocation.toString())

				}
			}

			var cameraPositionState: CameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(userLocation, 14f))


			if(permissionGranted) {
				MapScreen(mapViewModel = mapViewModel, cameraPositionState)
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

	@SuppressLint("MissingPermission")
	private fun startLocationUpdates() {
		locationCallback?.let {
			val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
				setMinUpdateDistanceMeters(minimalDistance)
				setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
				setWaitForAccurateLocation(true)
			}.build()
			fusedLocationClient?.requestLocationUpdates(
				locationRequest,
				it,
				Looper.getMainLooper()
			)
		}
	}
	/*
	override fun onResume(){
		super.onResume()
		if (isPermissionGranted()){
			startLocationUpdates()
		}
	}

	override fun onPause(){
		super.onPause()
		locationCallback?.let{
			fusedLocationClient?.removeLocationUpdates(it)
		}
	}
	*/
}
