package com.example.prosjekt_team18.ui.viewmodels

import com.example.prosjekt_team18.data.sunrise.SunData
import com.example.prosjekt_team18.data.weather.WeatherModel

data class SunWeatherUiState (
    val status: Status = Status.Loading,
    val currentWeather: WeatherModel? = null,
    val sunData: SunData? = null,
    val pinnedCurrentWeather: WeatherModel? = null,
    val pinnedSunData: SunData? = null,
)
