package com.example.prosjekt_team18.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.util.*

@Composable
fun FeedbackPage(modifier: Modifier = Modifier) {
	val calendar by remember {
		mutableStateOf(Calendar.getInstance().time)
	}
	var date by remember {
		mutableStateOf(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar))
	}
	Column(modifier = modifier.fillMaxSize()) {
		Text(modifier = Modifier.padding(20.dp),text = "Sjekkliste:")

		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(start = 20.dp), text = "Innenfor en grønn sone:")

			Icon(modifier = modifier
				.wrapContentWidth(Alignment.End)
				.padding(end = 20.dp),
				painter = painterResource(
				//if(approved) {
					id = com.example.prosjekt_team18.R.drawable.icons8_done_128
				//}
				//else {
				//id = com.example.prosjekt_team18.R.drawable.icons8_close_128) }
			),
				contentDescription = "Approved")
		}

		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(start = 20.dp), text = "Nok sollys:")

			Icon(modifier = modifier
				.wrapContentWidth(Alignment.End)
				.padding(end = 20.dp),
				painter = painterResource(
					//if(approved) {
					id = com.example.prosjekt_team18.R.drawable.icons8_done_128
					//}
					//else {
					//id = com.example.prosjekt_team18.R.drawable.icons8_close_128) }
				),
				contentDescription = "Approved")
		}

		Divider(modifier = Modifier
						.fillMaxWidth()
						.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(start = 20.dp), text = "Ikke noe regn:")

			Icon(modifier = modifier
				.wrapContentWidth(Alignment.End)
				.padding(end = 20.dp),
				painter = painterResource(
					//if(approved) {
					id = com.example.prosjekt_team18.R.drawable.icons8_done_128
					//}
					//else {
					//id = com.example.prosjekt_team18.R.drawable.icons8_close_128) }
				),
				contentDescription = "Approved")
		}

		Divider(modifier = Modifier
					.fillMaxWidth()
					.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(start = 20.dp), text = "Vindhastighet lavere enn 10m/s:")

			Icon(modifier = modifier
				.wrapContentWidth(Alignment.End)
				.padding(end = 20.dp),
				painter = painterResource(
					//if(approved) {
					id = com.example.prosjekt_team18.R.drawable.icons8_done_128
					//}
					//else {
					//id = com.example.prosjekt_team18.R.drawable.icons8_close_128) }
				),
				contentDescription = "Approved")
		}

		Divider(modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp), thickness = 2.dp)

		Row(modifier = Modifier.fillMaxWidth()) {
			Text(modifier = modifier.padding(start = 20.dp), text = "Ikke noe snø:")

			Icon(modifier = modifier
				.wrapContentWidth(Alignment.End)
				.padding(end = 20.dp),
				painter = painterResource(
					//if(approved) {
					id = com.example.prosjekt_team18.R.drawable.icons8_done_128
					//}
					//else {
					//id = com.example.prosjekt_team18.R.drawable.icons8_close_128) }
				),
				contentDescription = "Approved")
		}
	}
}