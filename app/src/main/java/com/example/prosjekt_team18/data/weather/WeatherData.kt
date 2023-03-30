package com.example.prosjekt_team18.data.weather

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
    val wind_from_direction: String,
    val wind_speed: String,
)


data class ForecastAtTime(
    val time: Date,
    val data: ForecastData
)

data class ForecastData(
    val instant: ForecastInstant,
    val next_6_hours: Forecast6Hours,
)
data class ForecastInstant (
    val details: ForecastInstantDetails,
)
data class ForecastInstantDetails (
    val air_temperature: Double,
    val wind_from_direction: Double,
    val wind_speed: Double,
)

data class Forecast6Hours (
    val details: Forecast6HoursDetails,
)
data class Forecast6HoursDetails (
    val precipitation_amount: Double
)