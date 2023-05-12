package com.example.prosjekt_team18

import com.example.prosjekt_team18.data.weather.WeatherDataSource
import com.example.prosjekt_team18.data.weather.WeatherModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.*

class WeatherDataSourceTest {
    private lateinit var weatherDataSource: WeatherDataSource

    @Before
    fun setUp() {
        weatherDataSource = WeatherDataSource()
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    // runTest is a coroutine builder designed for testing
//    fun testGetWeatherDataReturnsCorrectDate() = runTest {
//        // Arrange
//        // ?lat=60.10&lon=9.58
//        val lat = 60.10
//        val lon = 9.58
//
//        // Act
//        val result: WeatherModel = weatherDataSource.getWeatherData(lat, lon)
//        // Assert
//        assertEquals(Date(), result.date)
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetWeatherDataReturnsCorrectTemp() = runTest {
        // Arrange
        // ?lat=60.10&lon=9.58
        val lat = 60.10
        val lon = 9.58

        // Act
        val result: WeatherModel = weatherDataSource.getWeatherData(lat, lon)
        // Assert
        assertEquals(-0.7, result.temperature)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetWeatherDataReturnsCorrectRain() = runTest {
        // Arrange
        // ?lat=60.10&lon=9.58
        val lat = 60.10
        val lon = 9.58

        // Act
        val result: WeatherModel = weatherDataSource.getWeatherData(lat, lon)
        // Assert
        assertEquals(0.0, result.rainNextHour)
    }

    @OptIn(ExperimentalCoroutinesApi::class)

    @Test

    fun testGetWeatherDataReturnsCorrectWindDirection() = runTest {
        // Arrange
        // ?lat=60.10&lon=9.58
        val lat = 60.10
        val lon = 9.58

        // Act
        val result: WeatherModel = weatherDataSource.getWeatherData(lat, lon)
        // Assert
        assertEquals(122.6, result.windDirection)
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test

    fun testGetWeatherDataReturnsCorrectWindSpeed() = runTest {
        // Arrange
        // ?lat=60.10&lon=9.58
        val lat = 60.10
        val lon = 9.58

        // Act
        val result: WeatherModel = weatherDataSource.getWeatherData(lat, lon)
        // Assert
        assertEquals(1.4, result.windSpeed)
    }



}