package com.example.prosjekt_team18.ui.viewmodels

import com.example.prosjekt_team18.data.weather.WeatherModel

data class WeatherUiState (
    val status: Status = Status.Loading,
    val currentWeather: WeatherModel? = null
)
