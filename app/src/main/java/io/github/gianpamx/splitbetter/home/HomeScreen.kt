package io.github.gianpamx.splitbetter.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import io.github.gianpamx.splitbetter.R
import io.github.gianpamx.splitbetter.app.AppAction.CloseExpenseEditor
import io.github.gianpamx.splitbetter.app.AppAction.OpenExpenseEditor
import io.github.gianpamx.splitbetter.edit.EditExpenseScreen
import io.github.gianpamx.splitbetter.viewexpenses.ViewExpensesScreen
import io.github.gianpamx.splitbetter.viewexpenses.containerAnimationInMillis

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Expanded)
    )

    val configuration = LocalConfiguration.current

    var selectedExpenseId: String? by rememberSaveable { mutableStateOf(null) }
    LaunchedEffect(key1 = "appActions") {
        viewModel.actions.collect { appAction ->
            when (appAction) {
                is OpenExpenseEditor -> {
                    selectedExpenseId = appAction.expense.id
                }
                is CloseExpenseEditor -> {
                    selectedExpenseId = null
                }
                else -> Unit
            }
        }
    }
    val transition = updateTransition(selectedExpenseId != null, label = "transition")
    val height by transition.animateDp(
        label = "height",
        transitionSpec = { tween(containerAnimationInMillis) },
        targetValueByState = { isItSelected ->
            configuration.screenHeightDp.dp * if (isItSelected) 1.0f else 0.8f
        }
    )
    val roundCornerRadio by transition.animateDp(
        label = "roundCorners",
        transitionSpec = { tween(containerAnimationInMillis) },
        targetValueByState = { isItSelected ->
            if (isItSelected) 0.dp else 12.dp
        }
    )
    BottomSheetScaffold(
        topBar = { TopAppBarContent() },
        scaffoldState = bottomSheetScaffoldState,
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = roundCornerRadio,
            topEnd = roundCornerRadio
        ),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedExpenseId == null,
                enter = fadeIn(animationSpec = tween(containerAnimationInMillis)) + scaleIn(animationSpec = tween(containerAnimationInMillis)),
                exit = scaleOut(animationSpec = tween(containerAnimationInMillis)) + fadeOut(animationSpec = tween(containerAnimationInMillis)),
                ) {
                FloatingActionButton(contentColor = Color.White, onClick = {
                    viewModel.addExpense()
                }) {
                    Icon(painter = painterResource(R.drawable.ic_add), contentDescription = null)
                }
            }
        },
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(height)
            ) {
                Crossfade(targetState = selectedExpenseId, animationSpec = tween(containerAnimationInMillis)) { expenseId ->
                    if (expenseId != null) {
                        EditExpenseScreen(expenseId)
                    } else {
                        ViewExpensesScreen()
                    }
                }
            }
        }, sheetPeekHeight = 56.dp
    ) {
        BackgroundContent(state, Modifier.padding(it))
    }
}

@Composable
private fun TopAppBarContent() {
    TopAppBar(
        elevation = 0.dp,
        title = { Text(stringResource(R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.9f),
        contentPadding = rememberInsetsPaddingValues(
            LocalWindowInsets.current.statusBars,
            applyBottom = false,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
