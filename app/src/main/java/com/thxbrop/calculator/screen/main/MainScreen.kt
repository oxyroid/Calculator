package com.thxbrop.calculator.screen.main

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ui.Scaffold
import com.thxbrop.calculator.R
import com.thxbrop.calculator.ifTrue

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val state by viewModel.state
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayMedium
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LaunchedEffect(state.message) {
            state.message.handle {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            val listState = rememberLazyListState()
            LaunchedEffect(state.pre) {
                listState.animateScrollToItem(state.pre.size)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom,
                state = listState
            ) {
                items(state.pre) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.outline) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                val duration = state.scroll.ifTrue { 200 } ?: 0
                val animation =
                    slideInVertically(tween(duration)) { it } + fadeIn(tween(duration)) with
                            slideOutVertically(tween(duration)) { -it } + fadeOut(tween(duration))

                AnimatedContent(
                    targetState = state.formula,
                    transitionSpec = {
                        animation.using(
                            SizeTransform(true)
                        )
                    }
                ) { target ->
                    Text(
                        text = target.joinToString(separator = ""),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    CalculatorButton(text = "AC") {
                        viewModel.onEvent(MainEvent.Clear)
                    }
                }

                item {
                    CalculatorButton(text = "⬅") {
                        viewModel.onEvent(MainEvent.Drop)
                    }
                }
                item {
                    CalculatorButton(text = "％") {
                        viewModel.onEvent(MainEvent.Percent)
                    }
                }
                item {
                    CalculatorButton(text = "÷") {
                        viewModel.onEvent(MainEvent.Append("÷"))
                    }
                }
                item {
                    CalculatorButton(text = "7") {
                        viewModel.onEvent(MainEvent.Append("7"))
                    }
                }

                item {
                    CalculatorButton(text = "8") {
                        viewModel.onEvent(MainEvent.Append("8"))
                    }
                }
                item {
                    CalculatorButton(text = "9") {
                        viewModel.onEvent(MainEvent.Append("9"))
                    }
                }
                item {
                    CalculatorButton(text = "×") {
                        viewModel.onEvent(MainEvent.Append("×"))
                    }
                }
                item {
                    CalculatorButton(text = "4") {
                        viewModel.onEvent(MainEvent.Append("4"))
                    }
                }

                item {
                    CalculatorButton(text = "5") {
                        viewModel.onEvent(MainEvent.Append("5"))
                    }
                }
                item {
                    CalculatorButton(text = "6") {
                        viewModel.onEvent(MainEvent.Append("6"))
                    }
                }
                item {
                    CalculatorButton(text = "-") {
                        viewModel.onEvent(MainEvent.Append("-"))
                    }
                }
                item {
                    CalculatorButton(text = "1") {
                        viewModel.onEvent(MainEvent.Append("1"))
                    }
                }

                item {
                    CalculatorButton(text = "2") {
                        viewModel.onEvent(MainEvent.Append("2"))
                    }
                }
                item {
                    CalculatorButton(text = "3") {
                        viewModel.onEvent(MainEvent.Append("3"))
                    }
                }
                item {
                    CalculatorButton(text = "+") {
                        viewModel.onEvent(MainEvent.Append("+"))
                    }
                }
                item {
                    CalculatorButton(text = " ")
                }

                item {
                    CalculatorButton(text = "0") {
                        viewModel.onEvent(MainEvent.Append("0"))
                    }
                }
                item {
                    CalculatorButton(text = ".") {
                        viewModel.onEvent(MainEvent.Decimal)
                    }
                }
                item {
                    CalculatorButton(text = "=") {
                        viewModel.onEvent(MainEvent.Calculate)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatorButton(
    text: String,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(25),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        onClick = onClick,
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}