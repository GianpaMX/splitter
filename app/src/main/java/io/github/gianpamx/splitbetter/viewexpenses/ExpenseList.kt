package io.github.gianpamx.splitbetter.viewexpenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.gianpamx.splitbetter.R
import io.github.gianpamx.splitbetter.domain.entity.Expense
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun ExpenseList(
    expenseList: List<Expense>,
    onExpenseClick: ((Expense) -> Unit)?
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentPadding = PaddingValues(vertical = 8.dp),
        state = listState,
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.view_expenses_list_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        }
        items(expenseList, key = { it.id }) { expense ->
            ExpenseRow(expense = expense, onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(expenseList.indexOf(expense))
                    delay(containerAnimationInMillis.toLong())
                    onExpenseClick?.invoke(it)
                }
            })
        }
    }
}
