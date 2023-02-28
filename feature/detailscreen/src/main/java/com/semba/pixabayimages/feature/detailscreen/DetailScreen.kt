package com.semba.pixabayimages.feature.detailscreen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.semba.pixabayimages.core.design.component.ErrorView
import com.semba.pixabayimages.core.design.component.LoadingView
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.core.design.R as DesignR

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DetailRoute(modifier: Modifier = Modifier, viewModel: DetailViewModel = hiltViewModel(), imageId: Long) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.fetchImageItem(imageId)
    }

    DetailScreen(modifier = modifier, uiState = uiState)
}

@Composable
fun DetailScreen(modifier: Modifier = Modifier, uiState: DetailUiState) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is DetailUiState.Error -> DetailContent(showError = true)
            DetailUiState.Loading -> DetailContent(showLoading = true)
            is DetailUiState.Success -> {
                DetailContent(imageItem = uiState.imageItem)
            }
        }
    }
}

@Composable
fun DetailContent(imageItem: ImageItem = ImageItem.empty(), showLoading: Boolean = false, showError: Boolean = false) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background))
    {
        Box(modifier = Modifier
            .fillMaxSize()) {
            DetailImageSection(imageItem)
            DetailTopBar()
        }

        if (showLoading)
        {
            LoadingView(modifier = Modifier.align(Alignment.Center))
        }

        if (showError)
        {
            ErrorView(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun DetailTopBar() {
    Box(modifier = Modifier.padding(10.dp)) {
        val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

        IconButton(onClick = { dispatcher?.onBackPressed() }, modifier = Modifier
            .size(35.dp)
            .align(Alignment.CenterStart)) {
            Icon(modifier = Modifier.size(40.dp), painter = painterResource(id = DesignR.drawable.ic_back), contentDescription = "back_button", tint = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
fun DetailImageSection(imageItem: ImageItem) {
    Box(modifier = Modifier.fillMaxSize()) {
        ImagePreview(imageItem.fullHDURL)
        BottomDetailsSection(modifier = Modifier.align(Alignment.BottomCenter), imageItem.user, imageItem.userImageURL, imageItem.likes, imageItem.comments, imageItem.downloads, imageItem.views, imageItem.tags)
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
                    scaleX = maxOf(1f, minOf(3f, scale)),
                    scaleY = maxOf(1f, minOf(3f, scale))
                ),
            model = imageURL,
            contentDescription = ""
        )
    }
}

@Composable
fun BottomDetailsSection(modifier: Modifier = Modifier, userName: String, userImage: String, likes: Int, comments: Int, downloads: Int, views: Int, tags: String) {
    Column(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.outline),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        StatsViews(likes, comments, downloads, views)
        Spacer(modifier = Modifier.height(5.dp))
        UserProfile(userName, userImage)
        Spacer(modifier = Modifier.height(5.dp))
        TagChips(tags)
    }
}

@Composable
fun StatsViews(likes: Int, comments: Int, downloads: Int, views: Int) {
    Box(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()) {
        Row(modifier = Modifier.align(Alignment.CenterStart), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            StatItem(name = likes.toString(), icon = DesignR.drawable.ic_likes, tint = Color.Red)
            StatItem(name = comments.toString(), icon = DesignR.drawable.ic_comments)
            StatItem(name = downloads.toString(), icon = DesignR.drawable.ic_downloads)
        }
        StatItem(modifier = Modifier.align(Alignment.CenterEnd), name = views.toString(), icon = DesignR.drawable.ic_views)
    }
}

@Composable
fun StatItem(modifier: Modifier = Modifier, name: String, icon: Int, tint: Color = Color.White) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(30.dp), tint = tint)
        Text(text = name, fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
fun UserProfile(userName: String, userImage: String) {
    Row(
        Modifier
            .wrapContentSize()
            .padding(5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CircleImage(modifier = Modifier, image = userImage, size = 60.dp)
        Text(text = userName, color = MaterialTheme.colorScheme.onPrimary, fontSize = 15.sp)
    }
}

@Composable
fun CircleImage(modifier: Modifier = Modifier, image: String, size: Dp) {
    Box(modifier = modifier
        .size(size)
        .aspectRatio(1f, matchHeightConstraintsFirst = true)
        .border(1.dp, MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
        .padding(3.dp))
    {
        AsyncImage(model = image, contentDescription = "CircleImage", Modifier.clip(
            CircleShape
        ))
    }
}

@Composable
fun TagChips(tagsString: String) {
    val tags by remember {
        derivedStateOf {
            tagsString.split(", ")
        }
    }
    
    LazyRow(contentPadding = PaddingValues(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(tags.size) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(7.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            {
                Text(modifier = Modifier
                    .padding(5.dp, 5.dp, 5.dp, 5.dp), text = tags[it], color = MaterialTheme.colorScheme.onTertiary, fontSize = 13.sp)
            }
        }
    }
}
