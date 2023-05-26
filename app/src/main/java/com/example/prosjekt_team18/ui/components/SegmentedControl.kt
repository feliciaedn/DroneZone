package com.example.prosjekt_team18.ui.components

import androidx.annotation.ColorRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.prosjekt_team18.R

/**
 * Konstruerer en "switch"-knapp som velger mellom markør eller brukerens posisjon.
 * Denne brukes i værsiden og begrunnelsessiden for å bestemme om man vil ha
 * tilbakemelding basert på egen lokasjon eller markør-lokasjonen.
 */
@Composable
fun SegmentedControl(
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    itemWidth: Dp = 120.dp,
    cornerRadius : Int = 10,
    @ColorRes color : Int = R.color.dark_blue,
    onItemSelection: (selectedItemIndex: Int) -> Unit,
    pinnedLocation: Boolean
) {
    val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }

    Row(
        modifier = Modifier.padding(10.dp)
    ) {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = when (index) {
                    0 -> {
                        Modifier
                            .wrapContentSize()
                            .offset(0.dp, 0.dp)
                            .zIndex(if (selectedIndex.value == 0) 1f else 0f)
                    } else -> {
                        Modifier
                            .wrapContentSize()
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedIndex.value == index) 1f else 0f)
                    }
                },
                enabled = !(index == 1 && !pinnedLocation),

                onClick = {
                        selectedIndex.value = index
                        onItemSelection(selectedIndex.value)
                        println("dette er det som er: $index")
                },
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStartPercent = cornerRadius,
                        topEndPercent = 0,
                        bottomStartPercent = cornerRadius,
                        bottomEndPercent = 0
                    )
                    items.size - 1 -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = cornerRadius,
                        bottomStartPercent = 0,
                        bottomEndPercent = cornerRadius
                    )
                    else -> RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomStartPercent = 0,
                        bottomEndPercent = 0
                    )
                },
                border = BorderStroke(
                    1.dp, if (selectedIndex.value == index) {
                        colorResource(id = color)
                    } else {
                        colorResource(id = color).copy(alpha = 0.75f)
                    }
                ),
                colors = if (selectedIndex.value == index) {
                    // selected colors
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = colorResource(
                            id = color
                        )
                    )
                } else {
                    // not selected colors
                    if (index == 1 && !pinnedLocation) {
                        ButtonDefaults.outlinedButtonColors(backgroundColor = Color(230, 230, 240))
                    } else {
                        ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    }
                },
            ) {
                if (index == 0) {
                    val imgId = if (selectedIndex.value == 0) {
                        R.drawable.my_location_24_white
                    } else {
                        R.drawable.my_location_24_blue
                    }
                    Image(
                        painter = painterResource(id = imgId),
                        contentDescription = item,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    val imgId = if (selectedIndex.value == 1) {
                        R.drawable.pin_24_white
                    } else if (pinnedLocation) {
                        R.drawable.pin_24_blue
                    } else {
                        R.drawable.pin_24_gray
                    }
                    Image(
                        painter = painterResource(id = imgId),
                        contentDescription = item,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
        }
    }
}