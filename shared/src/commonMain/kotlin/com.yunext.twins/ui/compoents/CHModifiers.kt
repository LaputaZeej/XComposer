package com.yunext.twins.ui.compoents

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import com.yunext.twins.ui.theme.app_background_brush
import kotlinx.coroutines.CoroutineScope

fun Modifier.clickableX(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = composed(inspectorInfo = debugInspectorInfo {
    name = "pressed"
    properties["enabled"] = enabled
    properties["onClickLabel"] = onClickLabel
    properties["role"] = role
    properties["onClick"] = onClick
}) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val alpha = if (isPressed) .5f else 1f
     val scale = if (isPressed) 1.2f else 1f
    Modifier
        .clickable(
            interactionSource = interactionSource,
            indication = null,//LocalIndication.current,
            onClick = onClick,
            role = role,
            onClickLabel = onClickLabel,
            enabled = enabled
        )
        .alpha(alpha = alpha)
        .scale(scale)
        //.indication(interactionSource, if (isPressed) MyIndication() else null) // todo
}


/**
 * 详细见CommonRipple
 */
private open class MyIndication : Indication {
    open class Inner : IndicationInstance {


        override fun ContentDrawScope.drawIndication() {
            drawCircle(app_background_brush)
            draw()
            drawContent()
        }

        open fun add(interaction: PressInteraction.Press,scope: CoroutineScope){

        }

        open fun remove(interaction: PressInteraction.Press){

        }

        open fun ContentDrawScope.draw(){

        }


    }

    @Composable
    protected open fun rememberUpdateIndicationInstance(interactionSource: InteractionSource): Inner {
        return remember(interactionSource) {
            Inner()
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val myIndication = rememberUpdateIndicationInstance(interactionSource)
        LaunchedEffect(key1 = myIndication ){
            interactionSource.interactions.collect{
                interaction->
                when(interaction){
                    is PressInteraction.Press->{
                        myIndication.add(interaction,this)
                    }
                    is PressInteraction.Release->{
                        myIndication.remove(interaction.press)
                    }
                    is PressInteraction.Cancel->{
                        myIndication.remove(interaction.press)
                    }
                }
            }
        }
        return myIndication
    }

}