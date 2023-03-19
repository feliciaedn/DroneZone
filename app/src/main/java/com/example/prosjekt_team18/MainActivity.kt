package com.example.prosjekt_team18

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.prosjekt_team18.ui.presentation.MapScreen
import com.example.prosjekt_team18.ui.presentation.MapViewModel
import com.example.prosjekt_team18.ui.theme.Prosjekt_team18Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapScreen(viewModel = MapViewModel())
        }
    }
}
