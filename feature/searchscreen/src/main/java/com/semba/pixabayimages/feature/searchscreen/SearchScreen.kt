package com.semba.pixabayimages.feature.searchscreen

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.semba.pixabayimages.core.design.navigation.ScreenDestination
import com.semba.pixabayimages.core.design.theme.TextField_Container_Color
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.feature.searchscreen.state.ScrollState
import com.semba.pixabayimages.feature.searchscreen.state.SearchUiState
import com.semba.pixabayimages.feature.searchscreen.state.TopBarState
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import com.semba.pixabayimages.core.design.R as DesignR

private val MinTopBarHeight = 96.dp
private val MaxTopBarHeight = 150.dp

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchScreen(navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit) {

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

    val viewModel: SearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val queryState = remember { viewModel.queryState }

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
            gridState,
            uiState,
            loadMore = { viewModel.loadNextPage() },
            onItemClick = { imageItem ->
                navigateTo(ScreenDestination.DETAIL, imageItem.toArgs())})

        CollapsingTopBar(modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { toolbarState.height.toDp() })
            .graphicsLayer { translationY = toolbarState.offset },
            progress = toolbarState.progress,
            queryState = queryState,
            onSearchClick = { viewModel.onSearchClick() })
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CollapsingTopBar(modifier: Modifier = Modifier, progress : Float = 0f, queryState: MutableState<String>, onSearchClick: () -> Unit) {

    val keyboard = LocalSoftwareKeyboardController.current

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
                        value = queryState.value,
                        onValueChange = { queryState.value = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboard?.hide()
                                onSearchClick()
                            }
                        ),
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
                        IconButton(onClick = { onSearchClick() },
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

const val CELL_COUNT = 2
@Composable
fun ImagesGrid(modifier: Modifier = Modifier, gridState: LazyGridState = rememberLazyGridState(), uiState: SearchUiState, loadMore: () -> Unit, onItemClick: (ImageItem) -> Unit) {

    val uiStateUpdated by rememberUpdatedState(newValue = uiState)
    val shouldLoadMore = remember {
        derivedStateOf {
            !uiStateUpdated.isLoading && !uiStateUpdated.limitReached && (gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -100) >= (gridState.layoutInfo.totalItemsCount - 6)
        }
    }

    LaunchedEffect(key1 = shouldLoadMore.value) {
        if (shouldLoadMore.value)
            loadMore()
    }

    LazyVerticalGrid(
        modifier = modifier.padding(start = 7.dp, end = 7.dp, bottom = 7.dp, top = 7.dp),
        state = gridState,
        columns = GridCells.Fixed(CELL_COUNT),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(uiStateUpdated.imageItems.size) {
            ImageGridItem(modifier = Modifier.clickable { onItemClick(uiStateUpdated.imageItems[it]) }, item = uiStateUpdated.imageItems[it])
        }

        if (uiStateUpdated.isLoading) {
            item(span = { GridItemSpan(CELL_COUNT) }) {
                LoadingItem()
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box() {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp).padding(10.dp).align(Alignment.Center)
        )
    }
}

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): TopBarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}