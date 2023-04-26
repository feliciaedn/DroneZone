package com.example.prosjekt_team18.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.example.prosjekt_team18.ui.viewmodels.SunWeatherUiState
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FeedbackPage(sunWeatherUiState: State<SunWeatherUiState>, modifier: Modifier = Modifier) {
	val calendar by remember {
		mutableStateOf(Calendar.getInstance().time)
	}
	var timeNow by remember {
		mutableStateOf(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar))
	}
	val sunData = sunWeatherUiState.value.sunData
	val weatherModel = sunWeatherUiState.value.currentWeather

	var sunriseTimeString: String? = null
	var sunsetTimeString: String? = null

	if (weatherModel != null && sunData != null) {
		sunriseTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(sunData.sunrise.time)
		sunsetTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(sunData.sunset.time) }

	var sunlightCheck by remember { mutableStateOf(sunlightFunction(timeNow, sunriseTimeString, sunsetTimeString)) }
	val sunlightImageResource = when(sunlightCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	var rainCheck by remember { mutableStateOf(rainFunction(weatherModel)) }
	val rainImageResource = when(rainCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	var snowCheck by remember { mutableStateOf(snowFunction(weatherModel)) }
	val snowImageResource = when(snowCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	var windCheck by remember { mutableStateOf(windFunction(weatherModel)) }
	val windImageResource = when(windCheck) {
		true -> com.example.prosjekt_team18.R.drawable.icons8_done_128
		else -> com.example.prosjekt_team18.R.drawable.icons8_close_128
	}

	var flightApproval by remember { mutableStateOf(checkApproval(sunlightCheck, rainCheck, windCheck, snowCheck)) }

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
				painter = painterResource(
				//if(approved) {
					id = com.example.prosjekt_team18.R.drawable.icons8_done_128
				//}
				//else {
				//id = com.example.prosjekt_team18.R.drawable.icons8_close_128) }
			),
				contentDescription = "")
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
				contentDescription = "")
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
				contentDescription = "")
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
				contentDescription = "")
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
				contentDescription = "")
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
				color = Color(51, 153, 51)
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

		/*
		if (weatherModel != null) {
			Text(modifier = Modifier
				.fillMaxWidth()
				.wrapContentWidth(Alignment.CenterHorizontally),
				text = weatherModel.windSpeed.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(51, 153, 51))
		}
		 */

		/*
		if (sunData != null) {
			if (sunriseTimeString != null) {
				Text(modifier = Modifier
					.fillMaxWidth()
					.wrapContentWidth(Alignment.CenterHorizontally),
					text = sunriseTimeString, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(51, 153, 51))
			}
		}
		*/
	}
}

fun sunlightFunction(timeNow: String, sunriseTimeString: String?, sunsetTimeString: String?): Boolean {
	return timeNow > sunriseTimeString.toString() && timeNow < sunsetTimeString.toString()
}

fun rainFunction(weatherModel: WeatherModel?): Boolean {
	if (weatherModel != null) {
		return weatherModel.rainNextHour < 0.1
	}
	return false
}

fun snowFunction(weatherModel: WeatherModel?): Boolean {
	if (weatherModel != null) {
		return weatherModel.summaryNextHour != "snow" && weatherModel.summaryNextHour != "sleet"
	}
	return false
}

fun windFunction(weatherModel: WeatherModel?): Boolean {
	if (weatherModel != null) {
		return weatherModel.windSpeed < 10.0
	}
	return false
}


fun checkApproval(
	sunlightCheck: Boolean,
	rainCheck: Boolean,
	snowCheck: Boolean,
	windCheck: Boolean
): Boolean {
	/*
	if(!) {
		return false
	}*/
	if(!sunlightCheck) {
		return false
	}
	if(!rainCheck) {
		return false
	}
	if(!snowCheck) {
		return false
	}
	if(!windCheck) {
		return false
	}
	return true
}