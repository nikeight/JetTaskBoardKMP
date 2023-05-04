package com.jettaskboard.multiplatform.ui.screens.board

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.jettaskboard.multiplatform.data.util.Constants
import com.jettaskboard.multiplatform.ui.components.zoomable.Zoomable
import com.jettaskboard.multiplatform.ui.components.zoomable.rememberZoomableState
import com.jettaskboard.multiplatform.ui.components.board.Board
import com.jettaskboard.multiplatform.ui.screens.board.changeBg.ChangeBoardBackgroundRoute
import com.jettaskboard.multiplatform.ui.theme.DefaultTaskBoardBGColor
import com.jettaskboard.multiplatform.util.asyncimage.AsyncImage
import com.jettaskboard.multiplatform.util.dropdown.JDropdownMenu
import com.jettaskboard.multiplatform.util.dropdown.JDropdownMenuItem
import com.jettaskboard.multiplatform.util.krouter.rememberViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun TaskBoardRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    isExpandedScreen: Boolean = false,
    navigateToCreateCard: (String) -> Unit = {},
    navigateToChangeBgScreen: (String) -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.surface,
    boardBg: String? = null
) {
    val viewModel = rememberViewModel(TaskBoardViewModel::class) { TaskBoardViewModel() }
    val boardBackground by viewModel.boardBackground.collectAsState(initial = Constants.DEFAULT_BOARD_BG)
    val lists = remember(viewModel.totalCards) { viewModel.lists }
    val expandedScreenState = viewModel.drawerScreenState.value
    var expandedPanel by remember { mutableStateOf(false) }
    var editModeEnabled by remember { mutableStateOf(false) }
    var saveClicked by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()
    val zoomableState = rememberZoomableState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(boardBg) {
        boardBg?.let { safeBoardBg ->
            if (safeBoardBg.isEmpty().not()) {
                viewModel.updateBoardBackground(safeBoardBg)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TaskBoardAppBar(
                isExpandedScreen = isExpandedScreen,
                onBackClick = onBackClick,
                title = viewModel.boardInfo.value.second,
                navigateToChangeBgScreen = { passedString ->
                    navigateToChangeBgScreen(passedString)
                },
                onHamBurgerIconClicked = {
                    expandedPanel = !expandedPanel
                },
                onSaveClicked = {
                    saveClicked = true
                },
                editModeEnabled = editModeEnabled
            )
        },
        floatingActionButton = {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FAB(
                    onClick = {},
                    painterResource("ic_zoom_in.xml")
                )
                FAB(
                    onClick = {
                        if (zoomableState.scale.value != 1f) {
                            coroutineScope.launch {
                                zoomableState.animateBy(
                                    zoomChange = 1 / zoomableState.scale.value,
                                    panChange = -zoomableState.offset.value,
                                    rotationChange = -zoomableState.rotation.value
                                )
                            }
                        }
                    },
                    painterResource("ic_zoom_out.xml")
                )
            }
        }
    ) { scaffoldPaddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues),
            color = DefaultTaskBoardBGColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                AsyncImage(
                    imageUrl = boardBackground,
                    contentDescription = "Board background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                    loadingPlaceholder = {}
                )

                Zoomable(
                    coroutineScope = coroutineScope,
                    zoomableState = zoomableState
                ) {
                    Board(
                        modifier = Modifier.fillMaxSize(),
                        lists = lists,
                        onAddNewCardClicked = { listId ->
                            viewModel.addNewCardInList(listId)
                            editModeEnabled = true
                        },
                        onTaskCardEdited = { cardId, listId, title ->
                            viewModel.editCardInList(cardId, listId, title)
                            editModeEnabled = false
                            saveClicked = false
                        },
                        onAddNewListClicked = { viewModel.addNewList() },
                        onCardMovedToDifferentList = { cardId, oldListId, newListId ->
                            viewModel.moveCardToDifferentList(
                                cardId, oldListId, newListId
                            )
                        },
                        navigateToCreateCard = navigateToCreateCard,
                        isExpandedScreen = isExpandedScreen,
                        saveClicked = saveClicked
                    )
                }

                if (isExpandedScreen) {
                    AnimatedVisibility(
                        enter = slideInHorizontally { it },
                        exit = slideOutHorizontally { it },
                        visible = expandedPanel,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.28f)
                                .drawWithCache {
                                    onDrawBehind {
                                        drawRect(
                                            color = backgroundColor
                                        )
                                    }
                                }
                                .shadow(elevation = 0.dp)
                        ) {
                            when (expandedScreenState) {
                                ExpandedBoardDrawerState.DRAWER_SCREEN_STATE -> {
                                    TaskBoardExpandedScreenDrawer(
                                        navigateToChangeBackgroundRoute = {
                                            viewModel.changeExpandedScreenState(
                                                ExpandedBoardDrawerState.CHANGE_BACKGROUND_SCREEN_STATE
                                            )
                                        }
                                    )
                                }

                                ExpandedBoardDrawerState.CHANGE_BACKGROUND_SCREEN_STATE -> {
                                    ChangeBoardBackgroundRoute(
                                        onBackClick = {
                                            viewModel.changeExpandedScreenState(
                                                ExpandedBoardDrawerState.DRAWER_SCREEN_STATE
                                            )
                                        },
                                        onImageSelected = { imageUri ->
                                            viewModel.updateBoardBackground(imageUri)
                                        }
                                    )
                                }

                                ExpandedBoardDrawerState.FILTER_SCREEN_STATE -> {
                                    // Do Nothing
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskBoardAppBar(
    isExpandedScreen: Boolean = false,
    onBackClick: () -> Unit,
    onSaveClicked: () -> Unit,
    title: String,
    editModeEnabled: Boolean,
    navigateToChangeBgScreen: (String) -> Unit,
    onHamBurgerIconClicked: () -> Unit = {}
) {
    var displayTaskBoardToolbarMenuState by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = { Text(text = title) },
        actions = {
            if (editModeEnabled) {
                IconButton(
                    onClick = { onSaveClicked() }) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Save card"
                    )
                }
            } else {
                if (isExpandedScreen) {
                    IconButton(
                        onClick = { onHamBurgerIconClicked() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Hamburger Menu"
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            displayTaskBoardToolbarMenuState = !displayTaskBoardToolbarMenuState
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Toolbar Menu"
                        )
                    }

                    // DropDown
                    JDropdownMenu(
                        expanded = displayTaskBoardToolbarMenuState,
                        onDismissRequest = { displayTaskBoardToolbarMenuState = false },
                    ) {
                        JDropdownMenuItem(
                            onSelect = { navigateToChangeBgScreen("") }
                        ) {
                            Icon(
                                painter = painterResource("ic_baseline_wallpaper_24.xml"),
                                modifier = Modifier.size(18.dp),
                                contentDescription = "star",
                                tint = Color(0xFFFFEB3B)
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "Change Background"
                            )
                        }

                        JDropdownMenuItem(
                            onSelect = { navigateToChangeBgScreen("") }
                        ) {
                            Icon(
                                painter = painterResource("ic_baseline_filter_list_24.xml"),
                                modifier = Modifier.size(18.dp),
                                contentDescription = "star",
                                tint = Color(0xFFFFEB3B)
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "Filter"
                            )
                        }

                        JDropdownMenuItem(
                            onSelect = { navigateToChangeBgScreen("") }
                        ) {
                            Icon(
                                painter = painterResource("ic_baseline_automation_icon.xml"),
                                modifier = Modifier.size(18.dp),
                                contentDescription = "star",
                                tint = Color(0xFFFFEB3B)
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "Automation"
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FAB(
    onClick: () -> Unit,
    painter: Painter
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            modifier = Modifier,
            painter = painter,
            tint = Color.White,
            contentDescription = null
        )
    }
}
