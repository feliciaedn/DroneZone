package com.example.prosjekt_team18.data.weather

/**
 * Denne filen inneholder dataklasser som brukes for aa deserialisere JSON data fra
 * Locationforecast API.
 */

import java.util.*

data class WeatherDataWrapper (
    val properties : WeatherData
)

data class WeatherData (
    val meta: WeatherMetaData,
    val timeseries: List<ForecastAtTime>,
)

data class WeatherMetaData(
    val units: WeatherUnits
)

data class WeatherUnits(
    val air_temperature: String,
    val precipitation_amount: String,
    val wind_speed: String,
)


data class ForecastAtTime(
    val time: Date,
    val data: ForecastData
)

data class ForecastData(
    val instant: ForecastInstant,
    val next_1_hours: Forecast1Hour,
)
data class ForecastInstant (
    val details: ForecastInstantDetails,
)
data class ForecastInstantDetails (
    val air_temperature: Double,
    val wind_speed: Double,
)

data class Forecast1Hour (
    val summary: ForecastSummary,
    val details: Forecast1HourDetails,
)

data class Forecast1HourDetails (
    val precipitation_amount: Double
)

data class ForecastSummary (
    val symbol_code: String
)