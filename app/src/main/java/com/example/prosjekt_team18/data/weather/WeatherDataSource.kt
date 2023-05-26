package com.example.prosjekt_team18.data.weather

/**
 * Denne klassen brukes for aa hente inn vaerdata asynkront fra MET sin Locationforecast API,
 * gjennom en proxyserver.
 */

import com.example.prosjekt_team18.BuildConfig
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
    /* Konfigurerer client */
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
        install(DefaultRequest) {
            header("X-Gravitee-API-Key", API_KEY)
        }

    }

    /** Funksjonen gjoer kall til proxyn og returnerer et objekt av WeatherModel.
     */
    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherModel {
        val url = "$BASE_URL?lat=$latitude&lon=$longitude"

        // Ktor brukes for aa parse JSON til et objekt av dataklassen WeatherDataWrapper
        val data: WeatherDataWrapper = client.get(url).body()

        // Henter ut variabler som trengs for aa lage en WeatherModel (forenkling av WeatherDataWrapper)
        val forecastAtTime = data.properties.timeseries[0]

        val date = forecastAtTime.time
        val temperature = forecastAtTime.data.instant.details.air_temperature
        val summaryCode = forecastAtTime.data.next_1_hours.summary.symbol_code
        val summary = summaryCode.replace("_", " ")

        val rain = forecastAtTime.data.next_1_hours.details.precipitation_amount
        val windSpeed = forecastAtTime.data.instant.details.wind_speed

        return WeatherModel(date, temperature, summaryCode, summary, rain, windSpeed)
    }
}