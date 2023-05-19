package com.example.prosjekt_team18.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel

@Composable
fun FeedbackPage(mapViewModel: MapViewModel, modifier: Modifier = Modifier) {

	val sunlightCheck by remember { mutableStateOf(mapViewModel.enoughSunlight()) }
	val sunlightImageResource = when(sunlightCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	val rainCheck by remember { mutableStateOf(mapViewModel.okRain()) }
	val rainImageResource = when(rainCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	val snowCheck by remember { mutableStateOf(mapViewModel.okSnow()) }
	val snowImageResource = when(snowCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	val windCheck by remember { mutableStateOf(mapViewModel.okWind()) }
	val windImageResource = when(windCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	val airportCheck by remember { mutableStateOf(mapViewModel.notInAirportZone()) }
	val	airportImageResource = when(airportCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	val flightApproval by remember { mutableStateOf(mapViewModel.checkApproval(sunlightCheck, rainCheck, windCheck, snowCheck, airportCheck)) }

	Column(modifier = modifier.fillMaxSize()) {
		Text(modifier = Modifier
			.fillMaxWidth()
			.wrapContentWidth(Alignment.CenterHorizontally)
			.padding(20.dp),
			text = "Sjekkliste for flyving", fontSize = 25.sp, fontWeight = FontWeight.Bold)

		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 10.dp), thickness = 10.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(20.dp),
				text = "Utenfor en rød sone:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

			Image(modifier = modifier
				.fillMaxWidth()
				.wrapContentWidth(Alignment.End)
				.padding(top = 5.dp, end = 20.dp),
				painter = painterResource(airportImageResource),
				contentDescription = if(airportCheck) {
					"godkjent lokasjon"
				} else {
					"Ikke godkjent lokasjon"
				})
		}


		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(20.dp),
				text = "Nok sollys:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

			Image(modifier = modifier
				.fillMaxWidth()
				.wrapContentWidth(Alignment.End)
				.padding(top = 5.dp, end = 20.dp),
				painter = painterResource(sunlightImageResource),
				contentDescription = if(sunlightCheck) {
					"godkjente sollysforhold"
				} else {
					"Ikke godkjente sollysforhold"
				})
		}



		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(20.dp),
				text = "Regn mindre enn 0.1mm:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

			Image(modifier = modifier
				.fillMaxWidth()
				.wrapContentWidth(Alignment.End)
				.padding(top = 5.dp, end = 20.dp),
				painter = painterResource(rainImageResource),
				contentDescription = if(rainCheck) {
					"godkjente regnforhold"
				} else {
					"Ikke godkjente regnforhold"
				})
		}



		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(20.dp),
				text = "Snøfritt vær:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

			Image(modifier = modifier
				.fillMaxWidth()
				.wrapContentWidth(Alignment.End)
				.padding(top = 5.dp, end = 20.dp),
				painter = painterResource(snowImageResource),
				contentDescription = if(snowCheck) {
					"godkjente snøforhold"
				} else {
					"Ikke godkjente snøforhold"
				})
		}


		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(20.dp),
				text = "Vindhastighet lavere enn 10m/s:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

			Image(modifier = modifier
				.fillMaxWidth()
				.wrapContentWidth(Alignment.End)
				.padding(top = 5.dp, end = 20.dp),
				painter = painterResource(windImageResource),
				contentDescription = if(windCheck) {
					"godkjente vindforhold"
				} else {
					"Ikke godkjente vindforhold"
				})
		}


		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp), thickness = 10.dp)

		if (flightApproval) {
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentWidth(Alignment.CenterHorizontally),
				text = "Godkjent",
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold,
				color = Color(45, 134, 45)
			)
		}
		else {
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentWidth(Alignment.CenterHorizontally),
				text = "Ikke godkjent",
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold,
				color = Color(204, 0, 0)
			)
		}
	}
}
