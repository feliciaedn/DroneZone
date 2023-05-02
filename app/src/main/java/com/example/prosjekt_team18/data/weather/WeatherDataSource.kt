package com.example.prosjekt_team18.data.weather

import android.net.http.HttpResponseCache.install
import com.example.prosjekt_team18.BuildConfig
//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*

private const val API_KEY = BuildConfig.API_KEY
private const val BASE_URL =
    "https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/2.0/compact"

class WeatherDataSource {
    // Konfigurerer client
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
        install(DefaultRequest) {
            header("X-Gravitee-API-Key", API_KEY)
        }

    }

    // ?lat=60.10&lon=9.58
    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherModel {
        val url = "$BASE_URL?lat=$latitude&lon=$longitude"

        val data: WeatherDataWrapper = client.get(url).body()

        val forecastAtTime = data.properties.timeseries[0]

        val date = forecastAtTime.time
        val temperature = forecastAtTime.data.instant.details.air_temperature
        val summaryCode = forecastAtTime.data.next_1_hours.summary.symbol_code
        val summary = summaryCode.replace("_", " ")

        val rain = forecastAtTime.data.next_1_hours.details.precipitation_amount
        val windDirection = forecastAtTime.data.instant.details.wind_from_direction
        val windSpeed = forecastAtTime.data.instant.details.wind_speed

        val metaData = data.properties.meta
        val tempUnit = metaData.units.air_temperature
        val rainUnit = metaData.units.precipitation_amount
        val windDirUnit = metaData.units.wind_from_direction
        val windSpeedUnit = metaData.units.wind_speed

        return WeatherModel(date, temperature, summaryCode, summary, rain, windDirection, windSpeed, tempUnit, rainUnit,
            windDirUnit, windSpeedUnit)
    }
}