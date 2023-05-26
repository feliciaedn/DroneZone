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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.prosjekt_team18.data.maps.LocationDetails
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

/**
 * Klassen inneholder applikasjonens main-metode, onCreate(), samt oppretter
 * en private MapViewModel-instans.
 */
class MainActivity : ComponentActivity() {

	private val mapViewModel: MapViewModel by viewModels()

	/**
	 * Funksjonen iverksetter hele applikasjonen og gjør relevante funksjonskall
	 * for oppstart av applikasjonen, samt visning av UI elementer.
	 */
    override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {

			/* Setup for Google Maps og places API */
			mapViewModel.fusedLocationClient =
				LocationServices.getFusedLocationProviderClient(this)

			Places.initialize(this.applicationContext, BuildConfig.GOOGLE_API_KEY)
			mapViewModel.placesClient = Places.createClient(this)

			/* SPOERR BRUKER OM TILGANG TIL ENHETENS LOKASJON */

			// buttonClicked er true hvis knappen som spoer om tilgang til lokasjon er trykket ned
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

			/* Hvis det er foerste gangen bruker starter appen eller bruker ikke har gitt tilgang
			* til lokasjon foer, vises en knapp for aa gi tilgang. Naar denne trykkes paa
			* aapnes popup slik at brukeren kan gi appen tilgang til posisjon
			*/

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
					},
					colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B467C))
				) {
					Text(text = if (permissionGranted) "Lokasjon godkjent" else "Gi tilgang til lokasjon")
				}
			}

			// Hvis bruker allerede har gitt tilgang, og det ikke er foerste gangen de starter appen,
			// starter hovedsiden med en gang
			if (permissionGranted && !startup) {
				// update your UI
				Toast.makeText(
					this@MainActivity,
					"Lokasjon godkjent",
					Toast.LENGTH_SHORT
				).show()
				startup = true
			}

			/* HENTER BRUKERENS POSISJON */
			if (!(ActivityCompat.checkSelfPermission(
					this,
					ACCESS_FINE_LOCATION
				) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
					this,
					Manifest.permission.ACCESS_COARSE_LOCATION
				) != PackageManager.PERMISSION_GRANTED)
			) {
				// Request current location
				mapViewModel.fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { _location: Location? ->
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
					}
				}
			}

			/* STILLER INN POSISJON FOR MAP OG VISER HOVEDSIDEN */
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

	/**
	 * Funksjonen sjekker om appen har tilgang til brukerens presise lokasjon,
	 * og returnerer true/false avhengig av om den har det.
	 */
	private fun isPermissionGranted(): Boolean {
		return ContextCompat.checkSelfPermission(
			this,
			ACCESS_FINE_LOCATION
		) == PackageManager.PERMISSION_GRANTED
	}
}
