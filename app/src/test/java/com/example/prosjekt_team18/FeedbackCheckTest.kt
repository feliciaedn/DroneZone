package com.example.prosjekt_team18

/**
 * Enhetstest for alle funksjoner i FeedbackCheckTest, unntatt funksjonen notInAirportZone().
 * Enhetstesten for notInAirportZone() ligger i mappen androidTest.
 * Det er skrevet to tester for hver funksjon: en hvor funksjonen skal returnere true, og en hvor
 * funksjonen skal returnere false
 */

import com.example.prosjekt_team18.data.FeedbackCheck
import com.example.prosjekt_team18.data.weather.WeatherModel
import org.junit.Test
import java.time.LocalDate

import java.time.ZoneOffset
import java.util.*

class FeedbackCheckTest {
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
        val feedbackCheck = FeedbackCheck()

        val testTime = Date.from(
            LocalDate.of(2023, 5, 10)
                .atTime(12, 0).toInstant(ZoneOffset.UTC)
        )

        // Act
        val enoughSunlight = feedbackCheck.enoughSunlight(sunrise, sunset, testTime)

        // Assert
        assert(enoughSunlight)

    }

    @Test
    fun testEnoughSunlightFalseBeforeSunrise() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val testTime = Date.from(
            LocalDate.of(2023, 5, 10)
                .atTime(5, 0).toInstant(ZoneOffset.UTC)
        )

        // Act
        val enoughSunlight = feedbackCheck.enoughSunlight(sunrise, sunset, testTime)

        // Assert
        assert(!enoughSunlight)

    }

    @Test
    fun testEnoughSunlightFalseAfterSunset() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val testTime = Date.from(
            LocalDate.of(2023, 5, 10)
                .atTime(19, 30).toInstant(ZoneOffset.UTC)
        )

        // Act
        val enoughSunlight = feedbackCheck.enoughSunlight(sunrise, sunset, testTime)

        // Assert
        assert(!enoughSunlight)

    }

    @Test
    fun testOkRainTrue() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 0.0, // <- value for check
            windSpeed = 0.0
        )

        // Act
        val okRain = feedbackCheck.okRain(weatherModel)

        // Assert
        assert(okRain)

    }

    @Test
    fun testOkRainFalse() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 6.0, // <- value for check
            windSpeed = 0.0
        )

        // Act
        val okRain = feedbackCheck.okRain(weatherModel)

        // Assert
        assert(!okRain)

    }

    @Test
    fun testOkSnowTrue() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "clearsky day", // <- value for check
            rainNextHour = 0.0,
            windSpeed = 0.0
        )

        // Act
        val okSnow = feedbackCheck.okSnow(weatherModel)

        // Assert
        assert(okSnow)

    }

    @Test
    fun testOkSnowFalse() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "heavysnow", // <- value for check
            rainNextHour = 0.0,
            windSpeed = 0.0
        )

        // Act
        val okSnow = feedbackCheck.okSnow(weatherModel)

        // Assert
        assert(!okSnow)

    }

    @Test
    fun testOkWindTrue() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 0.0,
            windSpeed = 4.0 // <- value for check
        )

        // Act
        val okWind = feedbackCheck.okWind(weatherModel)

        // Assert
        assert(okWind)

    }

    @Test
    fun testOkWindFalse() {

        // Arrange
        val feedbackCheck = FeedbackCheck()

        val weatherModel = WeatherModel(
            date = Date(),
            temperature = 6.0,
            summaryCode = "",
            summaryNextHour = "",
            rainNextHour = 0.0,
            windSpeed = 11.0 // <- value for check
        )

        // Act
        val okWind = feedbackCheck.okWind(weatherModel)

        // Assert
        assert(!okWind)

    }

    @Test
    fun testCheckApprovalTrue() {
        // Arrange
        val feedbackCheck = FeedbackCheck()

        // Act
        val approval = feedbackCheck.checkApproval(
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
        val feedbackCheck = FeedbackCheck()

        // Act
        val approval = feedbackCheck.checkApproval(
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