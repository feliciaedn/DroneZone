package com.example.prosjekt_team18.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prosjekt_team18.R

@Composable
fun RulePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Regler for å fly drone",
            modifier = modifier.padding(16.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp,color = Color(0xFF1B467C))
        )

        RulePageDescription(modifier)
    }
    RuleImageColumn(modifier)

}

@Composable
fun RulePageDescription(modifier: Modifier = Modifier) {
    // TEXT WITH HYPERLINK
    // Creating an annonated string
    val annotatedLinkString = buildAnnotatedString {

        val fullString = "Hentet fra Luftfartstilsynet. For mer informasjon om regler for flyving av drone, se Luftfartstilsynets nettside."

        // Hyperlink
        val linkStartIndex = fullString.indexOf("Luftfartstilsynets nettside")
        val linkEndIndex = linkStartIndex + "Luftfartstilsynets nettside".length

        append(fullString)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ), start = linkStartIndex, end = linkEndIndex
        )

        addStringAnnotation(
            tag = "URL",
            annotation = "https://luftfartstilsynet.no/droner/fly-drone-trygt/#:~:text=Hold%20dronen%20innenfor%20synsrekkevidde.,personer%2C%20se%20datatilsynet.no.",
            start = linkStartIndex,
            end = linkEndIndex
        )

    }

    val uriHandler = LocalUriHandler.current

    ClickableText(
        text = annotatedLinkString,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        onClick = {
            annotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}
@Composable
fun RuleImageColumn(modifier: Modifier = Modifier) {
    val ruleImageIds = listOf(
        R.drawable.regel1_registrering,
        R.drawable.regel2_forsikring,
        R.drawable.regel3_droneforbudssoner,
        R.drawable.regel4_sedronen,
        R.drawable.regel5_ikkeflyover,
        R.drawable.regel6_avstand
    )

    val ruleDescriptionIds = stringArrayResource(R.array.rule_descriptions)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var ruleNr = 1
        items(ruleImageIds) { ruleImageId ->
            RuleCard(ruleImageId, ruleDescriptionIds[ruleNr++ % ruleDescriptionIds.size], modifier)
            //RuleCard(ruleImageId, ruleDescriptionIds[ruleNr++], modifier)
        }
    }
}

@Composable
fun RuleCard(
    imageId: Int,
    ruleDescription: String,
    modifier: Modifier = Modifier,
) {
    Card (
        shape = RoundedCornerShape(10.dp),
        colors =  CardDefaults.cardColors(
            containerColor =  Color.White,
        ),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            // Merger alle elementer i carden for bedre tilgjenglighet
            .semantics(mergeDescendants = true) {},

        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()

        ) {

            Image (
                painter = painterResource(id = imageId),
                contentDescription = ruleDescription
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun RuleColumnPreview() {
    RulePage()
}