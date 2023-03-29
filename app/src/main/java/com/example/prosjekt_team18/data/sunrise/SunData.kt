package com.example.prosjekt_team18.data.sunrise

import java.time.ZonedDateTime
import java.util.*

data class SunDataWrapper (
    val properties : SunData
)

data class SunData (
    val sunrise: SunTime,
    val sunset: SunTime,
)

data class SunTime(
    val time: Date
)