package io.github.gianpamx.splitbetter.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import io.github.gianpamx.splitbetter.R
import java.text.NumberFormat

enum class Tab(val title: Int) {
    PAYERS(R.string.edit_expense_tab_payers_title),
    RECEIVERS(R.string.edit_expense_tab_receivers_title)
}

@Composable
internal fun EditExpenseAppBar(
    state: EditState,
    pagerState: PagerState,
    modifier: Modifier,
    backgroundColor: Color,
    onTabSelected: ((Tab) -> Unit)? = null,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit
) {
    var title by rememberSaveable { mutableStateOf(state.expense?.title.orEmpty()) }

    var isEditing by remember { mutableStateOf(false) }
    val total = NumberFormat.getCurrencyInstance()
        .format(state.total().inMayorCurrencyUnit())

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val contentColor = contentColorFor(backgroundColor = backgroundColor)
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (backButton, titleText, totalText, tabs) = createRefs()

            IconButton(onClick = {
                onBack()
            }, modifier = Modifier.constrainAs(backButton) {
                top.linkTo(parent.top, 8.dp)
                start.linkTo(parent.start)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }

            TextField(
                value = title,
                onValueChange = {
                    title = it
                    onTitleChange(it)
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { isEditing = it.isFocused }
                    .constrainAs(titleText) {
                        top.linkTo(backButton.top)
                        bottom.linkTo(backButton.bottom)
                        start.linkTo(backButton.end)
                        end.linkTo(parent.end, 16.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    },
                trailingIcon = {
                    Row {
                        AnimatedVisibility(visible = !isEditing) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                tint = Color.White,
                                contentDescription = "Edit",
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.White.copy(alpha = 0.2f),
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.White,
                ),
                textStyle = MaterialTheme.typography.h6,
            )

            Text(
                text = total,
                modifier = Modifier.constrainAs(totalText) {
                    start.linkTo(titleText.start)
                    top.linkTo(titleText.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
                style = MaterialTheme.typography.subtitle1
            )

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(modifier = Modifier.pagerTabIndicatorOffset(pagerState, tabPositions))
                },
                modifier = Modifier.constrainAs(tabs) {
                    top.linkTo(totalText.bottom)
                    width = Dimension.matchParent
                    height = Dimension.wrapContent
                },
            ) {
                Tab.values().forEach {
                    Tab(
                        selected = pagerState.currentPage == it.ordinal,
                        onClick = { onTabSelected?.invoke(it) },
                        text = { Text(stringResource(it.title)) })
                }
            }
        }
    }
}
