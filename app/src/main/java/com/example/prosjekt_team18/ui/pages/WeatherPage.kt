package com.example.prosjekt_team18.ui.pages

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.data.sunrise.SunData
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.example.prosjekt_team18.ui.viewmodels.SunWeatherUiState
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.text.DateFormat
import java.util.*

/**
 * Konstruerer værsiden, en Page som har som hensikt å informere brukeren om været,
 * samt solinformasjon både for egen lokasjon og en markør-lokasjon.
 */
@Composable
fun WeatherPage(sunWeatherUiState: State<SunWeatherUiState>, context: Context, userLocation: LatLng,
                showCurrentLocation: Boolean
){

    var weatherModel: WeatherModel? by remember { mutableStateOf(null)}
    var sunData: SunData? by remember { mutableStateOf(null)}

    if (showCurrentLocation) {
        weatherModel = sunWeatherUiState.value.currentWeather
        sunData = sunWeatherUiState.value.sunData
    } else {
        weatherModel = sunWeatherUiState.value.pinnedCurrentWeather
        sunData = sunWeatherUiState.value.pinnedSunData
    }

    val placeName : String // Navn på stedet

    val addressLine : String
    val locality : String

    var country by remember {
        mutableStateOf( "")
    }
    var by by remember {
        mutableStateOf( "")
    }

    var addressString by remember {
        mutableStateOf( "")
    }


    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address: Address = addresses[0]
            by = if (address.adminArea != null) {
                address.adminArea
            } else {
                ""
            }

            placeName = if (address.featureName != null) {
                address.featureName // Navn på stedet
            } else {
                ""
            }
            println("her er addressen folkens her her her: " + placeName)

            // addressLine = address.getAddressLine(0) // Adresse
            //         locality = address.locality // Lokalitet
            country = if (address.countryName != null) {
                address.countryName // Land
            } else {
                ""
            }

            addressString = if (by != "") {
                "$by, $country"
            } else {
                country
            }

        }
    } catch (e: IOException) {
        Toast.makeText(
            context,
            "Kunne ikke laste inn addressen. Sjekk at du er koblet til nettet.",
            Toast.LENGTH_SHORT
        ).show()
    }

    if (weatherModel != null && sunData != null) {
		val sunriseTimeString: String
		val sunsetTimeString: String
		if(sunData!!.sunrise.time != null) {
			sunriseTimeString = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.GERMANY).format(sunData!!.sunrise.time)
		} else {
			sunriseTimeString = "N/A"
		}
		if(sunData!!.sunset.time != null) {
			sunsetTimeString = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.GERMANY).format(sunData!!.sunset.time)
		} else {
			sunsetTimeString = "N/A"
		}

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {Text(
                addressString,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp,
                    color = Color(0xFF1B467C)
                )
            )}
//            println("her er ideeeennnenenen: " + getBilde(weatherModel.summaryCode))

//            Image(
//                painter = painterResource(id = getBilde(weatherModel.summaryCode)),
//                contentDescription = null,
//                modifier = Modifier.size(160.dp)
//            )
            val imageName = weatherModel!!.summaryCode // Dette er navnet på bildet du vil vise
            val resourceId = getStringToDrawableId( imageName,context)
            if (resourceId != 0) {
                // Riktig drawable-ressurs-ID ble funnet
//                val painter = painterResource(id = resourceId)
                item {Image (painter = painterResource(id = resourceId), contentDescription = null, modifier = Modifier.size(200.dp))}
            } else { println ("fungerer ikke")}


            item {Text(
                "${weatherModel!!.temperature}°",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    color = Color.Black
                )
            )}
            item {Text(
                weatherModel!!.summaryNextHour,
                style = TextStyle(fontSize = 17.sp, color = Color(0xFF1B467C))
            )}
            item{Column(
                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherCard(weatherModel!!, context)
                SunCard(sunriseTimeString, sunsetTimeString)

            }}
        }
    }
}

@Composable
fun WeatherCard(weatherModel: WeatherModel,context: Context) {
    Card (
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical=0.dp)
            .fillMaxWidth()
            .width(120.dp)
            .height(110.dp)
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
            //id = R.drawable._72922)
            Column() {
                Image (painter = painterResource(id = R.drawable._72922), contentDescription = null, modifier = Modifier.size(40.dp))
                Text("${weatherModel.windSpeed} m/s", style = TextStyle( fontSize = 16.sp,color = Color.Black))
                Text("Vind", style = TextStyle( fontSize = 16.sp,color = Color(0xFF1B467C)))

            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Column(){
                //(painter =

                Image (painter = painterResource(id = R.drawable._038403), contentDescription = null, modifier = Modifier.size(40.dp))

                Text("${weatherModel.rainNextHour} mm", style = TextStyle( fontSize = 16.sp,color = Color.Black))
                Text("Regn", style = TextStyle( fontSize = 16.sp,color = Color(0xFF1B467C)))
            }

        }
    }
}
fun getStringToDrawableId(stringValue: String, context: Context): Int {
    val resources = context.resources // Erstatt 'context' med din aktuelle kontekst
    val packageName = context.packageName // Erstatt 'context' med din aktuelle kontekst
    return resources.getIdentifier(stringValue, "drawable", packageName)
}
//
//fun getBilde(summaryCode: String) : Int{
//
//    val id = when (summaryCode) {
//        "lightrain" -> R.drawable.lightrain
//        "heavysnowshowers_polart.wilight" -> R.drawable.heavysnowshowers_polartwilight
//        "heavysnowshowers_day" -> R.drawable.heavysnowshowers_day
//        "lightsnowshowers_night" -> R.drawable.lightsnowshowers_night
//        "lightsnowshowers_polartwilight" -> R.drawable.lightsnowshowers_polartwilight
//        "lightsnowshowers_day" -> R.drawable.lightsnowshowers_day
//        "heavysleetshowers_night" -> R.drawable.heavysleetshowers_night
//        "heavysleetshowers_polartwilight" -> R.drawable.heavysleetshowers_polartwilight
//        "heavysleetshowers_day" -> R.drawable.heavysleetshowers_day
//        "lightsleetshowers_night" -> R.drawable.lightsleetshowers_night
//        "lightsleetshowers_polartwilight" -> R.drawable.lightsleetshowers_polartwilight
//        "heavyrainshowers_polartwilight" -> R.drawable.heavyrainshowers_polartwilight
//        "lightrainshowers_day" -> R.drawable.lightrainshowers_day
//        "lightrainandthunder" -> R.drawable.lightrainandthunder
//        "heavysnowshowersandthunder_day" -> R.drawable.heavysnowshowersandthunder_day
//        "lightssnowshowersandthunder_day" -> R.drawable.lightssnowshowersandthunder_day
//        "heavysleetshowersandthunder_day" -> R.drawable.heavysleetshowersandthunder_day
//        "heavyrainshowersandthunder_night" -> R.drawable.heavyrainshowersandthunder_night
//        "lightrainshowersandthunder_night" -> R.drawable.lightrainshowersandthunder_night
//        "snowshowersandthunder_night" -> R.drawable.snowshowersandthunder_night
//        "sleetshowersandthunder_polartwilight" -> R.drawable.sleetshowersandthunder_polartwilight
//        "rain" -> R.drawable.rain
//        "snowshowers_polartwilight" -> R.drawable.snowshowers_polartwilight
//        "snowshowers_day" -> R.drawable.snowshowers_day
//        "sleetshowers_night" -> R.drawable.sleetshowers_night
//        "sleetshowers_polartwilight" -> R.drawable.sleetshowers_polartwilight
//        "sleetshowers_day" -> R.drawable.sleetshowers_day
//        "rainshowersandthunder_night" -> R.drawable.rainshowersandthunder_night
//        "rainshowersandthunder_polartwilight" -> R.drawable.rainshowersandthunder_polartwilight
//        "rainshowersandthunder_day" -> R.drawable.rainshowersandthunder_day
//        "rainshowers_night" -> R.drawable.rainshowers_night
//        "rainshowers_polartwilight" -> R.drawable.rainshowers_polartwilight
//        "rainshowers_day" -> R.drawable.rainshowers_day
//        "cloudy" -> R.drawable.cloudy
//        "partlycloudy_night" -> R.drawable.partlycloudy_night
//        else ->  R.drawable.cloudy
//    }
//    return id
//}


@Composable
fun SunCard(sunriseTimeString: String, sunsetTimeString: String) {
    Card (
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .width(120.dp)
            .height(100.dp)
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
            Image (painter = painterResource(id = R.drawable.icons8_sunrise_90), contentDescription = null, modifier = Modifier.size(25.dp))
            Text(sunriseTimeString, style = TextStyle( fontSize = 16.sp,color = Color.Black))
            Text("Soloppgang", style = TextStyle( fontSize = 16.sp,color = Color(0xFF1B467C)))
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Column( ){
                Image (painter = painterResource(id = R.drawable.icons8_sunset_90), contentDescription = null, modifier = Modifier.size(25.dp))
                Text(sunsetTimeString, style = TextStyle( fontSize = 16.sp,color = Color.Black))
                Text("Solnedgang", style = TextStyle( fontSize = 16.sp,color = Color(0xFF1B467C)))

            }

        }
    }
}