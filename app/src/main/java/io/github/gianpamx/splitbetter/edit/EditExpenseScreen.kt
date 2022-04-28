package io.github.gianpamx.splitbetter.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.github.gianpamx.splitbetter.domain.entity.Expense
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.Payer
import io.github.gianpamx.splitbetter.domain.entity.Person
import io.github.gianpamx.splitbetter.domain.entity.Receiver
import kotlinx.coroutines.launch

@Composable
fun EditExpenseScreen(expenseId: String) {
    val viewModel: EditExpenseViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(expenseId) {
        if (expenseId.isEmpty())
            viewModel.newExpense()
        else
            viewModel.loadExpense(expenseId)
    }

    if (state.expense == null) return

    EditExpenseContent(
        state = state,
        onEditExpense = { expense ->
            viewModel.saveExpenseAndExit(expense)
        },
        onPayerClicked = { payer ->
            viewModel.selectPayer(payer)
        },
        onAddNewPayerClicked = {
            viewModel.addNewPayer()
        },
        onReceiverCheckedChange = { receiver ->
            viewModel.updateReceiver(receiver)
        },
    )

    state.selectedPayer?.let {
        EditPayerDialog(
            selectedPayer = it,
            onConfirm = { person, payedAmount ->
                viewModel.savePayer(person = person, payedAmount = payedAmount)
            },
            onDismiss = {
                viewModel.removeSelectedPayer()
            }
        )
    }
}

@Composable
internal fun EditExpenseContent(
    state: EditState,
    onEditExpense: ((Expense) -> Unit)? = null,
    onPayerClicked: ((Payer) -> Unit)? = null,
    onAddNewPayerClicked: (() -> Unit)? = null,
    onReceiverCheckedChange: ((Receiver) -> Unit)? = null,
    onAddNewReceiverClick: (() -> Unit)? = null,
) {
    var title by rememberSaveable { mutableStateOf(state.expense?.title.orEmpty()) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val onBack: () -> Unit = {
        state.expense?.let { onEditExpense?.invoke(it.copy(title = title)) }
    }

    BackHandler(onBack = onBack)

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (appBar, list) = createRefs()

        EditExpenseAppBar(
            state = state,
            pagerState = pagerState,
            modifier = Modifier.constrainAs(appBar) {
                top.linkTo(parent.top)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            },
            backgroundColor = MaterialTheme.colors.primarySurface,
            onBack = onBack,
            onTitleChange = { title = it },
            onTabSelected = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it.ordinal)
                }
            },
        )

        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier.constrainAs(list) {
                top.linkTo(appBar.bottom)
                bottom.linkTo(parent.bottom)
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
            },
        ) { page ->
            when (page) {
                Tab.PAYERS.ordinal -> PayerList(
                    payers = state.payers,
                    modifier = Modifier.fillMaxSize(),
                    onClick = onPayerClicked,
                    onAddNewPersonClick = onAddNewPayerClicked,
                )
                Tab.RECEIVERS.ordinal -> ReceiverList(
                    receivers = state.receivers,
                    modifier = Modifier.fillMaxSize(),
                    onCheckedChange = onReceiverCheckedChange,
                    onAddNewPersonClick = onAddNewReceiverClick,
                )
            }
        }
    }
}

@Composable
fun ReceiverList(
    receivers: List<Receiver>,
    modifier: Modifier = Modifier,
    onCheckedChange: ((Receiver) -> Unit)? = null,
    onAddNewPersonClick: (() -> Unit)? = null
) {
    LazyColumn(modifier = modifier) {
        items(receivers, key = { it.person.id }) { receiver ->
            ReceiverRow(receiver, onCheckedChange)
        }
        item(key = "AddNewPayer") {
            AddNewPerson(onAddNewPersonClick)
        }
    }

}

@Composable
fun ReceiverRow(receiver: Receiver, onCheckedChange: ((Receiver) -> Unit)? = null) {
    var isChecked by remember { mutableStateOf(receiver.isChecked) }
    ConstraintLayout(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable {
                isChecked = !isChecked
                onCheckedChange?.invoke(receiver.copy(isChecked = isChecked))
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        val (name, checkbox) = createRefs()

        Text(receiver.person.name, modifier = Modifier.constrainAs(name) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(checkbox.start, 16.dp)
            height = Dimension.wrapContent
            width = Dimension.fillToConstraints
        })

        Checkbox(isChecked, modifier = Modifier.constrainAs(checkbox) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(name.end)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
            width = Dimension.wrapContent
        }, onCheckedChange = {
            isChecked = it
            onCheckedChange?.invoke(receiver.copy(isChecked = isChecked))
        })
    }
}

@Preview
@Composable
private fun EditExpenseContentPreview() = EditExpenseContent(
    state = EditState(
        expense = Expense(
            id = "",
            title = "Title",
            payers = listOf(
                dummyPayer()
            ),
        ),
        payers = listOf(dummyPayer())
    ),
)

fun dummyPayer(id: String = "1", name: String = "First last", cents: Int = 123456) = Payer(
    Person(
        id = id,
        name = name
    ),
    amount = Money(cents)
)

