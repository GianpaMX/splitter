package io.github.gianpamx.splitbetter.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import io.github.gianpamx.splitbetter.R
import io.github.gianpamx.splitbetter.domain.entity.Money
import io.github.gianpamx.splitbetter.domain.entity.Payer
import io.github.gianpamx.splitbetter.domain.entity.Person

@Composable
internal fun EditPayerDialog(
    selectedPayer: Payer,
    onConfirm: ((person: Person, payedAmount: Money) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    var payerName by rememberSaveable { mutableStateOf(selectedPayer.person.name) }
    var payedAmount by rememberSaveable { mutableStateOf(selectedPayer.amount.cents) }

    AlertDialog(
        onDismissRequest = {
            onDismiss?.invoke()
        },
        title = { Text(stringResource(R.string.edit_expense_payer_dialog_title)) },
        text = {
            Column {
                TextField(
                    value = payerName,
                    label = { Text(stringResource(R.string.edit_expense_payer_dialog_name_label)) },
                    onValueChange = { payerName = it },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                    )
                )
                TextField(
                    value = Money(payedAmount).toStringInMayorCurrencyUnit(),
                    label = { Text(stringResource(R.string.edit_expense_payer_dialog_amount_label)) },
                    onValueChange = { payedAmount = Money(it).cents },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm?.invoke(selectedPayer.person.copy(name = payerName), Money(payedAmount))
            }) {
                Text(stringResource(R.string.edit_expense_payer_dialog_confirm_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss?.invoke()
                }
            ) {
                Text(stringResource(R.string.edit_expense_payer_dialog_dismiss_button))
            }
        },
    )
}
