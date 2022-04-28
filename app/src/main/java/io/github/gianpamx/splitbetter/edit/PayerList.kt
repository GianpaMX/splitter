package io.github.gianpamx.splitbetter.edit

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.gianpamx.splitbetter.domain.entity.Payer

@Composable
internal fun PayerList(
    payers: List<Payer>,
    modifier: Modifier = Modifier,
    onClick: ((Payer) -> Unit)? = null,
    onAddNewPersonClick: (() -> Unit)? = null
) {
    LazyColumn(modifier = modifier) {
        items(payers, key = { it.person.id }) { payer ->
            PayerRow(payer, onClick)
        }
        item(key = "AddNewPayer") {
            AddNewPerson(onAddNewPersonClick)
        }
    }
}

