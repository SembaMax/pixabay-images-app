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
import com.semba.pixabayimages.data.model.search.ImageItem

const val CARD_HEIGHT = 200
const val BOTTOM_BOX_HEIGHT = 60
const val CARD_CONTENT_PADDING = 5

@Composable
fun ImageGridItem(modifier: Modifier = Modifier, item: ImageItem) {
    Card(modifier = modifier.height(CARD_HEIGHT.dp), shape = RoundedCornerShape(10.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(modifier = Modifier.fillMaxSize(), model = item.imageURL, contentDescription = null, contentScale = ContentScale.Crop)

            BottomBar(username = item.user, tags = item.tags)
        }
    }
}

@Composable
private fun BoxScope.BottomBar(username: String, tags: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(BOTTOM_BOX_HEIGHT.dp)
            .background(MaterialTheme.colorScheme.outline)
            .align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.SpaceEvenly,
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
                .padding(start = CARD_CONTENT_PADDING.dp, end = CARD_CONTENT_PADDING.dp)
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
                .padding(start = CARD_CONTENT_PADDING.dp, end = CARD_CONTENT_PADDING.dp)
        )
    }
}