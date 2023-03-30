package com.example.prosjekt_team18.data.sunrise

import java.util.*

data class WeatherModel (
    val date: Date,
    val temperature: Double,
    val rainNext6h: Double,
    val windDirection: Double,
    val windSpeed: Double,

    val tempUnit: String,
    val rainUnit: String,
    val windDirUnit: String,
    val windSpeedUnit: String,

)