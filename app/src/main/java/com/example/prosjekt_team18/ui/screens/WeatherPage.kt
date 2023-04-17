package com.example.prosjekt_team18.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import com.example.prosjekt_team18.ui.viewmodels.WeatherUiState


@Composable
fun WeatherPage(weatherUiState: State<WeatherUiState>){

    val weatherModel = weatherUiState.value.currentWeather

    if (weatherModel != null) {

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
            Image(
                painter = painterResource(id = R.drawable._11721_cloud_icon),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )
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
                WeatherCard(weatherModel)
                SunCard()

            }
        }
    }
}

@Composable
fun WeatherCard(weatherModel: WeatherModel) {
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
                Image (painter = painterResource(id = R.drawable._038403), contentDescription = null, modifier = Modifier.size(40.dp))
                Text("${weatherModel.rainNext6h} mm", style = TextStyle( fontSize = 10.sp,color = Color.Black))
                Text("Regn", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))
            }

        }
    }
}

@Composable
fun SunCard() {
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
            Text("06:08", style = TextStyle( fontSize = 10.sp,color = Color.Black))
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
                Text("20:27", style = TextStyle( fontSize = 10.sp,color = Color.Black))
                Text("Solnedgang", style = TextStyle( fontSize = 10.sp,color = Color(0xFF1B467C)))

            }

        }
    }
}