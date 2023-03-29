package com.example.prosjekt_team18.data.sunrise

import android.text.format.DateUtils
import android.util.Log
import com.example.prosjekt_team18.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.util.date.*
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

private val apiKey = BuildConfig.API_KEY
private const val BASE_URL =
    "https://gw-uio.intark.uh-it.no/in2000/weatherapi/sunrise/3.0/sun"

class SunDataSource {
    private var currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private var timeZone = TimeZone.getDefault()
    private var timezoneOffset = formatOffset(timeZone.getOffset(Date().time) / 60000)

    // Konfigurerer client
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
        install(DefaultRequest) {
            header("X-Gravitee-API-Key", apiKey)
        }

    }

    suspend fun getSunData(dateString: String = currentDate, latitude: Double, longitude: Double): SunData {
        val url = BASE_URL + "?lat=$latitude&lon=$longitude&date=$dateString&offset=$timezoneOffset"

        val data: SunDataWrapper = client.get(url).body()

        return data.properties
    }

    // Returnerer timezone offset paa riktig format, f.eks. +02:00
    fun formatOffset(offsetInMinutes: Int): String {
        val hours = abs(offsetInMinutes / 60)
        val mins = abs(offsetInMinutes % 60)

        return if (offsetInMinutes >= 0) {
            String.format("+%02d:%02d", hours, mins)
        } else {
            String.format("-%02d:%02d", hours, mins)
        }

    }
}