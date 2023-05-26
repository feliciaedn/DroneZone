package com.example.prosjekt_team18.data.weather

/* Denne dataklassen tar vare paa data returnert fra Locationforecast API.
 * WeatherModel er laget som en forenkling av dataklassen WeatherData, som brukes for
 * deserialisering av JSON data fra API-kallet. Ved bruk av WeatherModel kan noedvendig data
 * aksesseres enklere
 */

import java.util.*

data class WeatherModel (
    val date: Date,
    val temperature: Double,
    val summaryCode: String,
    val summaryNextHour: String,
    val rainNextHour: Double,
    val windSpeed: Double,

)