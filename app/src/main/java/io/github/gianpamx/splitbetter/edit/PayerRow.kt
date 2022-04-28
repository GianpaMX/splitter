package io.github.gianpamx.splitbetter.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.github.gianpamx.splitbetter.domain.entity.Payer

@Composable
internal fun PayerRow(payer: Payer, onClick: ((Payer) -> Unit)? = null) {
    ConstraintLayout(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable {
                onClick?.invoke(payer)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        val (name, amount) = createRefs()

        Text(payer.person.name, modifier = Modifier.constrainAs(name) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(amount.start, 16.dp)
            height = Dimension.wrapContent
            width = Dimension.fillToConstraints
        })

        Text("${payer.amount.inMayorCurrencyUnit()}", modifier = Modifier.constrainAs(amount) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(name.end)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
            width = Dimension.wrapContent
        })
    }
}
