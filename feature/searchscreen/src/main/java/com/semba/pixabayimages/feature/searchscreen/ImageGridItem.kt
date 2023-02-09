package com.semba.pixabayimages.feature.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ImageGridItem(modifier: Modifier = Modifier) {
    Card(modifier = modifier.height(300.dp), shape = RoundedCornerShape(10.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(modifier = Modifier.fillMaxSize(), model = "https://friendofthesea.org/wp-content/uploads/baby-sea-turtles-fos-certification-small.jpg", contentDescription = null, contentScale = ContentScale.Crop)

            BottomBar(username = "Sea Turtule", tags = "sea, turtules, ocean, marine, water, underwater, wild, wildlife")
        }
    }
}

@Composable
private fun BoxScope.BottomBar(username: String, tags: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.outline)
            .align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = username,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = tags,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}