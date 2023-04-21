package com.example.prosjekt_team18.ui.viewmodels

data class ScreenUiState(
    val showSearchBar: Boolean = false,
    val showWeather: Boolean = false,
	val showFeedback: Boolean = false,
    val showSheet: Sheet = Sheet.None,
)

enum class Sheet {
    Weather, Rules, Feedback, None
}