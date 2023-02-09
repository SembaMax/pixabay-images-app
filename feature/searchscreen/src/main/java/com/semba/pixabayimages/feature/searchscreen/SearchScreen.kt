package com.semba.pixabayimages.feature.searchscreen

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.semba.pixabayimages.core.design.theme.TextField_Container_Color
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import com.semba.pixabayimages.core.design.R as DesignR

private val MinTopBarHeight = 96.dp
private val MaxTopBarHeight = 150.dp

@Preview
@Composable
fun SearchScreen() {

    val toolbarHeightRange = with(LocalDensity.current) {
        MinTopBarHeight.roundToPx()..MaxTopBarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val gridState = rememberLazyGridState()

    val scope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached = gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 0) {
                    scope.launch {
                        animateDecay(
                            initialValue = toolbarState.height + toolbarState.offset,
                            initialVelocity = available.y,
                            animationSpec = FloatExponentialDecaySpec()
                        ) { value, velocity ->
                            toolbarState.scrollTopLimitReached = gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
                            toolbarState.scrollOffset = toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                            if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                        }
                    }
                }

                return super.onPostFling(consumed, available)
            }
        }
    }

    Box(modifier = Modifier
        .nestedScroll(nestedScrollConnection)
        .background(MaterialTheme.colorScheme.background)) {
        ImagesGrid(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { scope.coroutineContext.cancelChildren() }
                    )
                },
            gridState)
        CollapsingTopBar(modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { toolbarState.height.toDp() })
            .graphicsLayer { translationY = toolbarState.offset },
            progress = toolbarState.progress)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopBar(modifier: Modifier = Modifier, progress : Float = 0f) {

    var text by remember {
        mutableStateOf("")
    }

    Surface(modifier = modifier) {

        Box(modifier = Modifier
            .fillMaxSize()) {

            Image(modifier = Modifier.fillMaxSize(), painter = painterResource(id = DesignR.drawable.topbar_background), contentDescription = null, contentScale = ContentScale.Crop)

            Column(modifier = Modifier
                .fillMaxSize()
            ) {

                Text(text = stringResource(id = DesignR.string.search_headline),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                    modifier = Modifier.weight(0.8f),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = TextField_Container_Color,
                            textColor = Color.DarkGray,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                    singleLine = true)

                    Box(modifier = Modifier
                        .weight(0.2f)) {
                        IconButton(onClick = {},
                            Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .align(Alignment.Center)) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp),
                                painter = painterResource(id = DesignR.drawable.ic_search),
                                contentDescription = "search_icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImagesGrid(modifier: Modifier = Modifier, gridState: LazyGridState = rememberLazyGridState()) {

    LazyVerticalGrid(
        modifier = modifier.padding(start = 7.dp, end = 7.dp, bottom = 7.dp, top = 7.dp),
        state = gridState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(40) {
            ImageGridItem()
        }
    }
}

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): TopBarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}