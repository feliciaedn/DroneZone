package com.example.prosjekt_team18.data

import com.example.prosjekt_team18.data.weather.WeatherModel
import java.text.DateFormat
import java.util.*

class FeedbackModel {
    private val calendar = Calendar.getInstance().time
    private var timeNow = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)

    fun sunlightFunction(sunriseTimeString: String?, sunsetTimeString: String?): Boolean {
        return timeNow > sunriseTimeString.toString() && timeNow < sunsetTimeString.toString()
    }

    fun rainFunction(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return weatherModel.rainNextHour < 0.1
        }
        return false
    }

    fun snowFunction(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return weatherModel.summaryNextHour != "snow" && weatherModel.summaryNextHour != "sleet"
        }
        return false
    }

    fun windFunction(weatherModel: WeatherModel?): Boolean {
        if (weatherModel != null) {
            return weatherModel.windSpeed < 10.0
        }
        return false
    }

    fun checkApproval(
        sunlightCheck: Boolean,
        rainCheck: Boolean,
        snowCheck: Boolean,
        windCheck: Boolean
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
        return true
    }
}