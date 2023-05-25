package com.example.prosjekt_team18.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.prosjekt_team18.ui.viewmodels.MapViewModel


@Composable
fun SearchBar(mapViewModel: MapViewModel) {

    val focusManager = LocalFocusManager.current

//svaret er lagret her
    Column(
        modifier = Modifier.padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

//        Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = mapViewModel.text,
            onValueChange = {
                mapViewModel.text = it
                mapViewModel.searchPlaces(it)
                println(mapViewModel.locationAutofill.toString())
            },
            placeholder = { androidx.compose.material.Text("Søk etter lokasjon") },
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null
                )

            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        if (mapViewModel.text.isEmpty()) {
                            focusManager.clearFocus()
                            mapViewModel.toggleShowSearchBar()
                        } else {
                            mapViewModel.text = ""
                            mapViewModel.locationAutofill.clear()
                        }
                    }
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() },
                onSearch = { focusManager.clearFocus() }
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
//                .fillMaxHeight()
                .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)

        )
//        }

            AnimatedVisibility(
                mapViewModel.locationAutofill.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mapViewModel.locationAutofill) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                mapViewModel.showMarker.value = false
                                mapViewModel.text = it.address
                                mapViewModel.locationAutofill.clear()
                                mapViewModel.getCoordinates(it)
                                focusManager.clearFocus()
                                //println("showMarker får ny verdi: " + mapViewModel.showMarker.value)
                            }) {
                            Text(text = it.address)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
}

data class AutocompleteResult(
    val address: String,
    val placeId: String,
)


