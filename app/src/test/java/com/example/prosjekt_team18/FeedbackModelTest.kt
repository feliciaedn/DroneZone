package com.example.prosjekt_team18

import android.location.Location
import com.example.prosjekt_team18.data.FeedbackModel
import com.example.prosjekt_team18.data.weather.WeatherModel
import com.google.android.gms.maps.model.LatLng
import io.ktor.util.date.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import junit.framework.TestCase.assertEquals

import java.time.ZoneOffset
import java.util.*

class FeedbackModelTest {
    private val sunrise: Date = Date.from(
        LocalDate.of(2023, 5, 10).atTime(6, 0)
            .toInstant(ZoneOffset.UTC)
    )

    private val sunset: Date = Date.from(
        LocalDate.of(2023, 5, 10).atTime(19, 0)
            .toInstant(ZoneOffset.UTC)
    )

    @Test
    fun testEnoughSunlightTrue() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val testTime = Date.from(
            LocalDate.of(2023, 5, 10)
                .atTime(12, 0).toInstant(ZoneOffset.UTC)
        )

        // Act
        val enoughSunlight = feedbackModel.enoughSunlight(sunrise, sunset, testTime)

        // Assert
        assert(enoughSunlight)

    }

    @Test
    fun testEnoughSunlightFalseBeforeSunrise() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val testTime = Date.from(
            LocalDate.of(2023, 5, 10)
                .atTime(5, 0).toInstant(ZoneOffset.UTC)
        )

        // Act
        val enoughSunlight = feedbackModel.enoughSunlight(sunrise, sunset, testTime)

        // Assert
        assert(!enoughSunlight)

    }

    @Test
    fun testEnoughSunlightFalseAfterSunset() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val testTime = Date.from(
            LocalDate.of(2023, 5, 10)
                .atTime(19, 30).toInstant(ZoneOffset.UTC)
        )

        // Act
        val enoughSunlight = feedbackModel.enoughSunlight(sunrise, sunset, testTime)

        // Assert
        assert(!enoughSunlight)

    }

    @Test
    fun testOkRainTrue() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 0.0, // <- value for check
            windDirection = 0.0,
            windSpeed = 0.0
        )

        // Act
        val okRain = feedbackModel.okRain(weatherModel)

        // Assert
        assert(okRain)

    }

    @Test
    fun testOkRainFalse() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 6.0, // <- value for check
            windDirection = 0.0,
            windSpeed = 0.0
        )

        // Act
        val okRain = feedbackModel.okRain(weatherModel)

        // Assert
        assert(!okRain)

    }

    @Test
    fun testOkSnowTrue() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "clearsky day", // <- value for check
            rainNextHour = 0.0,
            windDirection = 0.0,
            windSpeed = 0.0
        )

        // Act
        val okSnow = feedbackModel.okSnow(weatherModel)

        // Assert
        assert(okSnow)

    }

    @Test
    fun testOkSnowFalse() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "heavysnow", // <- value for check
            rainNextHour = 0.0,
            windDirection = 0.0,
            windSpeed = 0.0
        )

        // Act
        val okSnow = feedbackModel.okSnow(weatherModel)

        // Assert
        assert(!okSnow)

    }

    @Test
    fun testOkWindTrue() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 0.0,
            windDirection = 0.0,
            windSpeed = 4.0 // <- value for check
        )

        // Act
        val okWind = feedbackModel.okWind(weatherModel)

        // Assert
        assert(okWind)

    }

    @Test
    fun testOkWindFalse() {

        // Arrange
        val feedbackModel = FeedbackModel()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 0.0,
            windDirection = 0.0,
            windSpeed = 11.0 // <- value for check
        )

        // Act
        val okWind = feedbackModel.okWind(weatherModel)

        // Assert
        assert(!okWind)

    }

    @Test
    fun testCheckApprovalTrue() {
        // Arrange
        val feedbackModel = FeedbackModel()

        // Act
        val approval = feedbackModel.checkApproval(
            sunlightCheck = true,
            rainCheck = true,
            snowCheck = true,
            windCheck = true,
            airportCheck = true
        )

        // Assert
        assert(approval)

    }

    @Test
    fun testCheckApprovalFalse() {
        // Arrange
        val feedbackModel = FeedbackModel()

        // Act
        val approval = feedbackModel.checkApproval(
            sunlightCheck = true,
            rainCheck = false,
            snowCheck = true,
            windCheck = true,
            airportCheck = true
        )

        // Assert
        assert(!approval)

    }


}