package com.example.prosjekt_team18

import com.example.prosjekt_team18.data.sunrise.SunData
import com.example.prosjekt_team18.data.sunrise.SunDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.*

class SunDataSourceTest {
    private lateinit var sunDataSource: SunDataSource
    private lateinit var cal: Calendar

    @Before
    fun setUp() {
        sunDataSource = SunDataSource()

        // Date for test: 2023-03-26
        cal = Calendar.getInstance()
        cal[Calendar.YEAR] = 2023
        cal[Calendar.MONTH] = Calendar.MARCH
        cal[Calendar.DAY_OF_MONTH] = 26
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    // runTest is a coroutine builder designed for testing
    fun testGetSunDataReturnsCorrectSunrise() = runTest {
        // Arrange

        // (Expected sunrise 07:01)
        cal[Calendar.HOUR_OF_DAY] = 7
        cal[Calendar.MINUTE] = 1
        val date: Date = cal.time

        val dateString= "2023-03-26"
        val lat = 59.933333
        val lon = 10.716667

        // Act
        val result: SunData = sunDataSource.getSunData(dateString, lat, lon)


        // Assert
        assertEquals(date, result.sunrise.time)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetSunDataReturnsCorrectSunset() = runTest {
        // Arrange

        // (Expected sunset 19:45)
        cal[Calendar.HOUR_OF_DAY] = 19
        cal[Calendar.MINUTE] = 45
        val date: Date = cal.time

        val dateString= "2023-03-26"
        val lat = 59.933333
        val lon = 10.716667

        // Act
        val result: SunData = sunDataSource.getSunData(dateString, lat, lon)


        // Assert
        assertEquals(date, result.sunset.time)
    }


}