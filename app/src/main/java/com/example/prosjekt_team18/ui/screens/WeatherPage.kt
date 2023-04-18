package com.example.prosjekt_team18.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.R
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.example.prosjekt_team18.ui.viewmodels.SunWeatherUiState
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun WeatherPage(sunWeatherUiState: State<SunWeatherUiState>, context: Context){

    val weatherModel = sunWeatherUiState.value.currentWeather
    val sunData = sunWeatherUiState.value.sunData

    if (weatherModel != null && sunData != null) {
        val sunriseTimeString = SimpleDateFormat("h:mm", Locale.getDefault()).format(sunData.sunrise.time)
        val sunsetTimeString = SimpleDateFormat("h:mm", Locale.getDefault()).format(sunData.sunset.time)
        println("HER ER DEN ------- " + weatherModel.summaryCode)
       // println("HER ER DEN HALLOOOOOOO " + getResources().getIdentifier(weatherModel.summaryCode, "drawable", getPackageName()))


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Oslo, Norway",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp,
                    color = Color(0xFF1B467C)
                )
            )
            println("her er ideeeennnenenen: " + getBilde(weatherModel.summaryCode))
            /*
            Image(
                painter = painterResource(id = getBilde(weatherModel.summaryCode)),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )

             */


            Text(
                "${weatherModel.temperature}°",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    color = Color.Black
                )
            )
            Text(
                weatherModel.summaryNext6h,
                style = TextStyle(fontSize = 17.sp, color = Color(0xFF1B467C))
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherCard(weatherModel, context)
                SunCard(sunriseTimeString, sunsetTimeString)

            }
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
            //id = R.drawable._72922)
            Column() {
                Image (painter = painterResource(id = R.drawable._72922), contentDescription = null, modifier = Modifier.size(40.dp))
                Text("${weatherModel.windSpeed} m/s", style = TextStyle( fontSize = 10.sp,color = Color.Black))
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
                //(painter = painterResource(id = R.drawable._038403),
                val imageName = weatherModel.summaryCode // Dette er navnet på bildet du vil vise
                val resourceId = getStringToDrawableId( imageName,context)
                if (resourceId != 0) {
                    // Riktig drawable-ressurs-ID ble funnet
                    val painter = painterResource(id = resourceId)
                    Image (painter = painter, contentDescription = null, modifier = Modifier.size(40.dp))
                } else { println ("fungerer ikke")}

                Text("${weatherModel.rainNext6h} mm", style = TextStyle( fontSize = 10.sp,color = Color.Black))
                Text("Regn", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))
            }

        }
    }
}
fun getStringToDrawableId(stringValue: String, context: Context): Int {
    val resources = context.resources // Erstatt 'context' med din aktuelle kontekst
    val packageName = context.packageName // Erstatt 'context' med din aktuelle kontekst
    return resources.getIdentifier(stringValue, "drawable", packageName)
}

fun getBilde(summaryCode: String) : Int{

    val id: Int = when (summaryCode) {
            "lightrain" -> R.drawable.lightrain
            "heavysnowshowers_polartwilight" -> R.drawable.heavysnowshowers_polartwilight
            "heavysnowshowers_day" -> R.drawable.heavysnowshowers_day
            "lightsnowshowers_night" -> R.drawable.lightsnowshowers_night
            "lightsnowshowers_polartwilight" -> R.drawable.lightsnowshowers_polartwilight
            "lightsnowshowers_day" -> R.drawable.lightsnowshowers_day
            "heavysleetshowers_night" -> R.drawable.heavysleetshowers_night
            "heavysleetshowers_polartwilight" -> R.drawable.heavysleetshowers_polartwilight
            "heavysleetshowers_day" -> R.drawable.heavysleetshowers_day
            "lightsleetshowers_night" -> R.drawable.lightsleetshowers_night
            "lightsleetshowers_polartwilight" -> R.drawable.lightsleetshowers_polartwilight
            "heavyrainshowers_polartwilight" -> R.drawable.heavyrainshowers_polartwilight
            "lightrainshowers_day" -> R.drawable.lightrainshowers_day
            "lightrainandthunder" -> R.drawable.lightrainandthunder
            "heavysnowshowersandthunder_day" -> R.drawable.heavysnowshowersandthunder_day
            "lightssnowshowersandthunder_day" -> R.drawable.lightssnowshowersandthunder_day
            "heavysleetshowersandthunder_day" -> R.drawable.heavysleetshowersandthunder_day
            "heavyrainshowersandthunder_night" -> R.drawable.heavyrainshowersandthunder_night
            "lightrainshowersandthunder_night" -> R.drawable.lightrainshowersandthunder_night
            "snowshowersandthunder_night" -> R.drawable.snowshowersandthunder_night
            "sleetshowersandthunder_polartwilight" -> R.drawable.sleetshowersandthunder_polartwilight
            "rain" -> R.drawable.rain
            "snowshowers_polartwilight" -> R.drawable.snowshowers_polartwilight
            "snowshowers_day" -> R.drawable.snowshowers_day
            "sleetshowers_night" -> R.drawable.sleetshowers_night
            "sleetshowers_polartwilight" -> R.drawable.sleetshowers_polartwilight
            "sleetshowers_day" -> R.drawable.sleetshowers_day
            "rainshowersandthunder_night" -> R.drawable.rainshowersandthunder_night
            "rainshowersandthunder_polartwilight" -> R.drawable.rainshowersandthunder_polartwilight
            "rainshowersandthunder_day" -> R.drawable.rainshowersandthunder_day
            "rainshowers_night" -> R.drawable.rainshowers_night
            "rainshowers_polartwilight" -> R.drawable.rainshowers_polartwilight
            "rainshowers_day" -> R.drawable.rainshowers_day
            "cloudy" -> R.drawable.cloudy
            "partlycloudy_night" -> R.drawable.partlycloudy_night
            else -> { R.drawable.cloudy }
        }

    return id

}








@Composable
fun SunCard(sunriseTimeString: String, sunsetTimeString: String) {
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
            Text(sunriseTimeString, style = TextStyle( fontSize = 10.sp,color = Color.Black))
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
                Text(sunsetTimeString, style = TextStyle( fontSize = 10.sp,color = Color.Black))
                Text("Solnedgang", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))

            }

        }
    }
}