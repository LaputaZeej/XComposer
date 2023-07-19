package com.yunext.twins.ui.compoents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CHPressedView(
    content: @Composable (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource =
        remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(

        modifier = modifier.clickable(interactionSource = interactionSource, indication = null) {
            onClick.invoke()
        }, contentAlignment = Alignment.Center
    ) {
//        AnimatedVisibility(visible = isPressed) {
//            if (isPressed) {
//                Row {
//                    icon()
//                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//                }
//            }
//        }
//        AnimatedContent(targetState = isPressed) {
            val scale = if (isPressed) 2f else 1f
            val alpha = if (isPressed) .5f else 1f
//            Box(
//                modifier = Modifier
//                    .alpha(alpha = alpha)
//                    .scale(scale, scale)
//            ) {
            content(isPressed)
//            }
//        }
    }
}

@Composable
private fun InteractionSource.collectIsPressedOrDraggedAsState(): State<Boolean> {
    val isPressedOrDragged = remember { mutableStateOf(false) }
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(key1 = isPressedOrDragged) {
        this@collectIsPressedOrDraggedAsState.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    interactions.add(interaction)
                }

                is PressInteraction.Release -> {
                    interactions.remove(interaction.press)
                }

                is PressInteraction.Cancel -> {
                    interactions.remove(interaction.press)
                }

                is DragInteraction.Start -> {
                    interactions.add(interaction)
                }

                is DragInteraction.Stop -> {
                    interactions.remove(interaction.start)
                }

                is DragInteraction.Cancel -> {
                    interactions.remove(interaction.start)
                }
            }
            isPressedOrDragged.value = interactions.isNotEmpty()
        }
    }

    return isPressedOrDragged

}