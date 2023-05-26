package com.example.prosjekt_team18.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment.Companion as Alignment1

/**
 * Konstruerer et varselvindu til brukeren dersom brukeren ikke er tilgang til nett, men
 * forsøker å aksessere en Page(begrunnelsessiden og værsiden) som krever nettverkstilgang.
 */
@Composable
fun PopupDialog(title: String, description: String) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
            ) {

                Box(
                    contentAlignment = Alignment1.Center,
                    modifier = Modifier.padding(15.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            ),
                            color = Color(0xFF1B467C)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = description,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Default,
                                textAlign = TextAlign.Center,
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { openDialog.value = false },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .height(50.dp).align(Alignment.CenterHorizontally)
                                .padding(bottom = 5.dp, top = 5.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B467C))
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PopupDialogPreview() {
    PopupDialog(
        title = "Kunne ikke laste inn værdata",
        description = "Sjekk at du er tilkoblet internet og prøv igjen."
    )
}
