package com.example.prosjekt_team18.data

import android.location.Location
import com.example.prosjekt_team18.data.resources.AirportData.latCoordinates
import com.example.prosjekt_team18.data.resources.AirportData.lngCoordinates
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * Klassen inneholder sjekk-funksjoner for å avgjøre om en bruker kan fly drone på en
 * gitt lokasjon, eller ikke.
 */
class FeedbackCheck {

	/**
	 * Funksjonen sjekker om en lokasjon har nok sollys til å fly drone.
	 * Dersom sunriseTime og/eller sunsetTime er null betyr det at brukeren
	 * befinner seg på en lokasjon med midnattssol, og i så fall returnerer funksjonen
	 * true. Ellers returnerer den true/false avhengig om klokkeslettet til brukeren
	 * er innenfor dagens intervall for sollys.
	 */
    fun enoughSunlight(sunriseTime: Date?, sunsetTime: Date?, timeNow: Date): Boolean {
		if(sunriseTime == null || sunsetTime == null ) {
			return true
		}
        return timeNow.after(sunriseTime) && timeNow.before(sunsetTime)
    }

	/**
	 * Funksjonen avgjør om det er lite nok regn til å fly drone. Returnerer true
	 * dersom regn for den neste timen er lavere enn 0,1mm. Hvis ikke returnerer
	 * den false.
	 */
    fun okRain(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return weatherModel.rainNextHour < 0.1
        }
        return false
    }

	/**
	 * Funksjonen avgjør om det er lite nok snø til å fly drone. Returnerer true
	 * dersom værmeldingen for lokasjonen ikke inneholder snø. Returnerer false
	 * dersom det er meldt snø eller sludd.
	 */
    fun okSnow(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return !weatherModel.summaryNextHour.contains("snow") && !weatherModel.summaryNextHour.contains("sleet")
        }
        return false
    }

	/**
	 * Funksjonen avgjør om det er lite nok vind til å fly drone. Returnerer true
	 * dersom vindhastigheten er lavere enn 10m/s. Hvis ikke returnerer
	 * den false.
	 */
    fun okWind(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return weatherModel.windSpeed < 10.0
        }
        return false
    }

	/**
	 * Funksjonen sjekker om en gitt lokasjon befinner seg innenfor en rød sone.
	 * Returnerer true dersom lokasjonen befinner seg utenfor en 5km radius
	 * av alle flyplassene. Hvis ikke returnerer den false.
	 */
    fun notInAirportZone(selectedLocation: LatLng): Boolean {
        for (i in latCoordinates.indices) {
            val airportLocation = LatLng(latCoordinates[i], lngCoordinates[i])

            val results = FloatArray(1)
            Location.distanceBetween(
                selectedLocation.latitude, selectedLocation.longitude,
                airportLocation.latitude, airportLocation.longitude, results
            )

            if (results[0] <= 5000) {
                return false
            }

        }
        return true
    }

	/**
	 * Funksjonen samler resultatene fra funksjonene ovenfor som input i form av
	 * variabler, og avgjør om alle reglene er godkjent. Returnerer true dersom
	 * alle reglene er godkjent. Returnerer false dersom minst ett av kravene ikke
	 * er godkjent.
	 */
    fun checkApproval(
        sunlightCheck: Boolean,
        rainCheck: Boolean,
        snowCheck: Boolean,
        windCheck: Boolean,
        airportCheck: Boolean
    ): Boolean {

        if(!sunlightCheck) {
            return false
        }
        if(!rainCheck) {
            return false
        }
        if(!snowCheck) {
            return false
        }
        if(!windCheck) {
            return false
        }
        if(!airportCheck) {
            return false
        }
        return true
    }
}