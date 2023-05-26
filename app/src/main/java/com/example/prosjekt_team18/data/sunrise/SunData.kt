package com.example.prosjekt_team18.data.sunrise

/* Denne filen inneholder dataklasser som brukes for aa deserialisere JSON data fra Sunrise API.
 */

import java.util.*

data class SunDataWrapper (
    val properties : SunData
)

data class SunData (
    val sunrise: SunTime,
    val sunset: SunTime,
)

data class SunTime(
    val time: Date,
)