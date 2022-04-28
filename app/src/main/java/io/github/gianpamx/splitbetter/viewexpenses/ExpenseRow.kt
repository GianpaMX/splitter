package io.github.gianpamx.splitbetter.viewexpenses

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.gianpamx.splitbetter.domain.entity.Expense

@Composable
internal fun ExpenseRow(expense: Expense, onClick: ((Expense) -> Unit)? = null) {
    val transitionState = remember { MutableTransitionState(AnimationState.NOT_STARTED) }
    val transition = updateTransition(transitionState, label = "transition")
    val elevation by transition.animateDp(
        label = "elevation",
        transitionSpec = { tween(containerAnimationInMillis / 2) },
        targetValueByState = { if (it.hasStarted()) 14.dp else 0.dp }
    )
    val height by transition.animateDp(
        label = "height",
        transitionSpec = { tween(containerAnimationInMillis / 2) },
        targetValueByState = { if (it.hasElevated()) LocalConfiguration.current.screenHeightDp.dp else 56.dp }
    )

    transitionState.onStateChange { animationState ->
        when (animationState) {
            AnimationState.ELEVATING,
            AnimationState.EXPANDING -> {
                transitionState.targetState = transitionState.currentState.nextStep()
            }
            else -> Unit
        }
    }

    Surface(elevation = elevation) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .clickable {
                    transitionState.targetState = transitionState.currentState.nextStep()
                    onClick?.invoke(expense)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = expense.title,
                modifier = Modifier
                    .weight(4f)
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = "${expense.total.inMayorCurrencyUnit()}",
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private enum class AnimationState {
    NOT_STARTED,
    ELEVATING,
    EXPANDING,
    ENDED
}

private fun AnimationState.nextStep() = when (this) {
    AnimationState.NOT_STARTED -> AnimationState.ELEVATING
    AnimationState.ELEVATING -> AnimationState.EXPANDING
    AnimationState.EXPANDING -> AnimationState.ENDED
    AnimationState.ENDED -> AnimationState.NOT_STARTED
}

private fun AnimationState.hasStarted() = this > AnimationState.NOT_STARTED
private fun AnimationState.hasElevated() = this > AnimationState.ELEVATING

private fun MutableTransitionState<AnimationState>.onStateChange(block: (AnimationState) -> Unit) {
    if (currentState == targetState) {
        block(this.currentState)
    }
}

