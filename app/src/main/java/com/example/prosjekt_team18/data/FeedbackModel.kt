package com.example.prosjekt_team18.data

import android.location.Location
import com.example.prosjekt_team18.data.resources.AirportData
import com.example.prosjekt_team18.data.resources.AirportData.latCoordinates
import com.example.prosjekt_team18.data.resources.AirportData.lngCoordinates
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.google.android.gms.maps.model.LatLng
import java.util.*

class FeedbackModel () {

    fun enoughSunlight(sunriseTime: Date, sunsetTime: Date, timeNow: Date): Boolean {
        return timeNow.after(sunriseTime) && timeNow.before(sunsetTime)
    }

    fun okRain(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return weatherModel.rainNextHour < 0.1
        }
        return false
    }

    fun okSnow(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return !weatherModel.summaryNextHour.contains("snow")
        }
        return false
    }

    fun okWind(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return weatherModel.windSpeed < 10.0
        }
        return false
    }


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