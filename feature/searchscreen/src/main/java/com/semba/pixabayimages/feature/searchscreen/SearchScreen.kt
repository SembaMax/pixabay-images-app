package com.semba.pixabayimages.feature.searchscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.semba.pixabayimages.core.design.component.ErrorView
import com.semba.pixabayimages.core.design.component.LoadingView
import com.semba.pixabayimages.core.design.navigation.ScreenDestination
import com.semba.pixabayimages.core.design.theme.TextField_Container_Color
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.feature.searchscreen.domain.SearchScreenContract
import com.semba.pixabayimages.feature.searchscreen.state.*
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import com.semba.pixabayimages.core.design.R as DesignR

private val MinTopBarHeight = 135.dp
private val MaxTopBarHeight = 150.dp

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SearchRoute(modifier: Modifier = Modifier, viewModel: SearchViewModel = hiltViewModel(), navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val queryState by viewModel.queryState.collectAsStateWithLifecycle()

    SearchScreen(uiState = uiState, modifier = modifier, queryState = queryState, navigateTo = navigateTo, contract = viewModel)
}

@Composable
fun SearchScreen(uiState: SearchUiState,
                 modifier: Modifier = Modifier,
                 queryState: String = "",
                 navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit = {_,_->},
                 contract: SearchScreenContract? = null) {

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

    Box(modifier = modifier
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
            loadMore = { contract?.loadNextPage() },
            onItemClick = { imageItem -> contract?.showConfirmationDialog(imageItem) })

        CollapsingTopBar(modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { toolbarState.height.toDp() })
            .graphicsLayer { translationY = toolbarState.offset },
            progress = toolbarState.progress,
            queryState = queryState,
            onSearchClick = { contract?.onSearchClick() },
            updateQuery = {query -> contract?.updateQuery(query)})

        ConfirmationDialog(modifier = Modifier.align(Alignment.Center),
            userName = uiState.currentClickedImage.user,
            showDialog = uiState.showDialog,
            onConfirm = {
                contract?.dismissConfirmationDialog()
                navigateTo(ScreenDestination.DETAIL, uiState.currentClickedImage.toArgs()) },
            onDismiss = { contract?.dismissConfirmationDialog() })
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CollapsingTopBar(modifier: Modifier = Modifier, progress : Float = 0f, queryState: String, onSearchClick: () -> Unit, updateQuery: (String) -> Unit) {

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
                        value = queryState,
                        onValueChange = { updateQuery(it) },
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

    val shouldLoadMore = remember {
        derivedStateOf {
            !uiState.isLoading && !uiState.limitReached && (gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -100) >= (gridState.layoutInfo.totalItemsCount - 6)
        }
    }

    val shouldShowError by remember {
        derivedStateOf {
            !uiState.errorMsg.isNullOrEmpty() && uiState.imageItems.isNullOrEmpty()
        }
    }

    LaunchedEffect(key1 = shouldLoadMore.value) {
        if (shouldLoadMore.value)
            loadMore()
    }

    LazyVerticalGrid(
        modifier = modifier
            .padding(start = 7.dp, end = 7.dp, bottom = 7.dp, top = 7.dp)
            .testTag("search_images_grid"),
        state = gridState,
        columns = GridCells.Fixed(CELL_COUNT),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        imagesGridContent(uiState.imageItems, CELL_COUNT, gridState, onItemClick)

        if (uiState.isLoading) {
            item(span = { GridItemSpan(CELL_COUNT) }) {
                LoadingItem()
            }
        }

        if (shouldShowError)
        {
            item(span = { GridItemSpan(CELL_COUNT) }) {
                ErrorItem()
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    userName: String,
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            modifier = modifier,
            title = { Text(stringResource(DesignR.string.dialog_open_image_title, userName), fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground, lineHeight = 18.sp) },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = stringResource(DesignR.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(DesignR.string.no))
                }
            }
        )
    }
}

@Composable
fun LoadingItem() {
    Box() {
        LoadingView(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Center),
            size = 40.dp,
            showText = false
        )
    }
}

@Composable
fun ErrorItem() {
    Box {
        ErrorView(modifier = Modifier
            .align(Alignment.Center))
    }
}

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): TopBarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}

private fun LazyGridScope.imagesGridContent(images: List<ImageItem>, columns: Int, state: LazyGridState, onItemClick: (ImageItem) -> Unit) {
    items(images.count()) { index ->
        val animation = tween<Float>(durationMillis = 500, delayMillis = 100, easing = LinearOutSlowInEasing)
        val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
        val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)
        val imageItem = images[index]
        ImageGridItem(modifier = Modifier
            .clickable { onItemClick(imageItem) }
            .graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale), item = imageItem)
    }
}

@Composable
fun scaleAndAlpha(
    args: ScaleAndAlphaArgs,
    animation: FiniteAnimationSpec<Float>
): Pair<Float, Float> {
    val transitionState = remember { MutableTransitionState(TransitionState.PLACING).apply { targetState = TransitionState.PLACED } }
    val transition = updateTransition(transitionState, label = "")
    val alpha by transition.animateFloat(transitionSpec = { animation }, label = "") { state ->
        when (state) {
            TransitionState.PLACING -> args.fromAlpha
            TransitionState.PLACED -> args.toAlpha
        }
    }
    val scale by transition.animateFloat(transitionSpec = { animation }, label = "") { state ->
        when (state) {
            TransitionState.PLACING -> args.fromScale
            TransitionState.PLACED -> args.toScale
        }
    }
    return alpha to scale
}
