package com.example.prosjekt_team18.data.weather

import java.util.*

data class WeatherModel (
    val date: Date,
    val temperature: Double,
    val summaryNext6h: String,
    val rainNext6h: Double,
    val windDirection: Double,
    val windSpeed: Double,

    val tempUnit: String,
    val rainUnit: String,
    val windDirUnit: String,
    val windSpeedUnit: String,

)