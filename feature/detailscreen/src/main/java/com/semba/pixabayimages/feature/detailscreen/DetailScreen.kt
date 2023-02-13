package com.semba.pixabayimages.feature.detailscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.semba.pixabayimages.core.design.R as DesignR

@Composable
fun DetailScreen(
    userName: String,
    userImage: String,
    imageUrl: String,
    likes: Int,
    comments: Int,
    views: Int,
    downloads: Int,
    tags: ArrayList<String>,
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)) {
        DetailTopBar()
        DetailImageSection(imageUrl, userName, userImage, likes, comments, downloads, views, tags)
    }

}

@Composable
fun DetailTopBar() {
    Box(modifier = Modifier.height(50.dp)) {
        val navController = rememberNavController()

        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier
            .size(40.dp)
            .align(Alignment.CenterStart)) {
            Icon(modifier = Modifier.size(40.dp), painter = painterResource(id = DesignR.drawable.ic_back), contentDescription = "back_button")
        }
    }
}

@Composable
fun DetailImageSection(imageUrl: String, userName: String, userImage: String, likes: Int, comments: Int, downloads: Int, views: Int, tags: ArrayList<String>) {
    Box(modifier = Modifier.fillMaxSize()) {
        ImagePreview(imageUrl)
        BottomDetailsSection(userName, userImage, likes, comments, downloads, views, tags)
    }
}

@Composable
fun ImagePreview(imageURL: String) {

    var scale by remember { mutableStateOf(1f) }

    Box(
        modifier = Modifier
            .background(color = Color.Transparent)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale *= zoom
                }
            },
    ) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = maxOf(.5f, minOf(3f, scale)),
                    scaleY = maxOf(.5f, minOf(3f, scale))
                ),
            model = imageURL,
            contentDescription = ""
        )
    }
}

@Composable
fun BottomDetailsSection(userName: String, userImage: String, likes: Int, comments: Int, downloads: Int, views: Int, tags: ArrayList<String>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(100.dp)
        .background(MaterialTheme.colorScheme.outline)) {
        StatsViews(likes, comments, downloads, views)
        Spacer(modifier = Modifier.height(15.dp))
        UserProfile(userName, userImage)
        Spacer(modifier = Modifier.height(25.dp))
        TagChips(tags)
    }
}

@Composable
fun StatsViews(likes: Int, comments: Int, downloads: Int, views: Int) {
    Box(modifier = Modifier
        .fillMaxWidth()) {
        Row(modifier = Modifier.align(Alignment.CenterStart), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            StatItem(name = likes.toString(), icon = DesignR.drawable.ic_likes, tint = Color.Red)
            StatItem(name = comments.toString(), icon = DesignR.drawable.ic_comments)
            StatItem(name = downloads.toString(), icon = DesignR.drawable.ic_downloads)
        }
        StatItem(modifier = Modifier.align(Alignment.CenterEnd), name = views.toString(), icon = DesignR.drawable.ic_views)
    }
}

@Composable
fun StatItem(modifier: Modifier = Modifier, name: String, icon: Int, tint: Color = Color.White) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(30.dp), tint = tint)
        Text(text = name, fontSize = 15.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
fun UserProfile(userName: String, userImage: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
        CircleImage(modifier = Modifier.weight(0.3f), image = userImage, size = 100.dp)
        Text(text = userName)
    }
}

@Composable
fun CircleImage(modifier: Modifier = Modifier, image: String, size: Dp) {
    Box(modifier = modifier
        .size(size)
        .aspectRatio(1f, matchHeightConstraintsFirst = true)
        .border(1.dp, Color.LightGray, shape = CircleShape)
        .padding(3.dp))
    {
        AsyncImage(model = image, contentDescription = "CircleImage", Modifier.clip(
            CircleShape
        ))
    }
}

@Composable
fun TagChips(tags: ArrayList<String>) {
    LazyRow() {
        items(tags.size) {
            Box(
                modifier = Modifier
                    .padding(3.dp, 5.dp, 3.dp, 5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            {
                Text(text = tags[it], color = MaterialTheme.colorScheme.onTertiary, fontSize = 12.sp)
            }
        }
    }
}
