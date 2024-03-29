package com.example.prosjekt_team18.data.sunrise

/**
 * Denne klassen brukes for aa hente inn vaerdata asynkront fra MET sin Sunrise API,
 * gjennom en proxyserver.
 */
import com.example.prosjekt_team18.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

private const val API_KEY = BuildConfig.API_KEY
private const val BASE_URL =
    "https://gw-uio.intark.uh-it.no/in2000/weatherapi/sunrise/3.0/sun"

class SunDataSource {

        /* Variabler for API-kallet */
        private var currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        private var timeZone = TimeZone.getDefault()
        // Tidsforskyvning aa sende inn som parameter i API-kallet, basert paa brukerens tidssone
        private var timezoneOffset = formatOffset(timeZone.getOffset(Date().time) / 60000)


        /* Konfigurerer client */
        private val client = HttpClient {
            install(ContentNegotiation) {
                gson()
            }
            install(DefaultRequest) {
                header("X-Gravitee-API-Key", API_KEY)
            }
        }

        /**
         * Funksjonen gjoer kall til proxyn og returnerer et objekt av SunData. SunData inneholder
         * tidspunkt for soloppgang og solnedgang. Hvis et tidspunkt ikke kunne hentes vil det vaere
         * lik null
         */
        suspend fun getSunData(dateString: String = currentDate, latitude: Double, longitude: Double): SunData {
            val url = "$BASE_URL?lat=$latitude&lon=$longitude&date=$dateString&offset=$timezoneOffset"

            // Ktor brukes for aa parse JSON til et objekt av dataklassen SunDataWrapper
            val data: SunDataWrapper = client.get(url).body()

            return data.properties
        }

        /**
         * Funksjonen tar inn timezone offset i minutter som argument,
         * og returnerer timezone offset i timer og minutter som en String paa riktig format,
         * f.eks. "+02:00".
         */
        private fun formatOffset(offsetInMinutes: Int): String {
            val hours = abs(offsetInMinutes / 60)
            val mins = abs(offsetInMinutes % 60)

            return if (offsetInMinutes >= 0) {
                String.format("+%02d:%02d", hours, mins)
            } else {
                String.format("-%02d:%02d", hours, mins)
            }

        }
}