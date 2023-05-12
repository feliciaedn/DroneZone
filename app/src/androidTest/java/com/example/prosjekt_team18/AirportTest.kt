package com.example.prosjekt_team18

import com.example.prosjekt_team18.data.FeedbackCheck
import com.google.android.gms.maps.model.LatLng
import org.junit.Test

class AirportTest {

    @Test
    fun notInAirportZoneTrue() {

        // Arrange
        val feedbackCheck = FeedbackCheck()
        // ifi
        val location = LatLng(59.9444, 10.7184)

        // Act
        val notInAirportZone = feedbackCheck.notInAirportZone(location)

        // Assert
        assert(notInAirportZone)

    }

    @Test
    fun notInAirportZoneFalse() {

        // Arrange
        val feedbackCheck = FeedbackCheck()
        // Gardermoen
        val location = LatLng(60.191750, 11.101218)

        // Act
        val notInAirportZone = feedbackCheck.notInAirportZone(location)

        // Assert
        assert(!notInAirportZone)

    }
}