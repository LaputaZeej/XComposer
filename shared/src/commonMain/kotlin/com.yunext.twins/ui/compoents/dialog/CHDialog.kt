package com.yunext.twins.ui.compoents.dialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.ui.compoents.China
import com.yunext.twins.ui.page.device.XPopContainer

internal object DialogDefaults {
    internal val DEFAULT_FONT_Color = China.w_yu_du_bai
    internal val DEFAULT_FONT_Size = 12.sp
    internal val DEFAULT_SIZE = 150.dp
    internal val DEFAULT_SHAPE = RoundedCornerShape(12.dp)
    internal val DEFAULT_DEBUG_COLOR = China.r_luo_xia_hong
    internal val DEFAULT_Progress_Color = China.w_yu_du_bai
    internal val DEFAULT_Padding = 16.dp
    internal val DEFAULT_Padding_hor = 64.dp
}

internal fun Modifier.debug(debug: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (debug) {
        block(this)
    } else this
}

internal fun Modifier.debugShape(debug: Boolean) = debug(debug) {
    border(
        1.dp, DialogDefaults.DEFAULT_DEBUG_COLOR, shape = DialogDefaults.DEFAULT_SHAPE
    )
}

@Composable
fun CHDialog(
    properties: DialogProperties = DialogProperties(
        decorFitsSystemWindows = true,
        usePlatformDefaultWidth = true,
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit
    ),
    dimAmount: Float = .1f,
    onDismissRequest: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,

) {
    XPopContainer(contentAlignment = Alignment.Center, onDismiss = {
        if (properties.dismissOnClickOutside) {
            onDismissRequest()
        }
    }, dimAmount = dimAmount, content = {
        content()
    })
}


@Immutable
class DialogProperties constructor(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    val usePlatformDefaultWidth: Boolean = true,
    val decorFitsSystemWindows: Boolean = true
) {

    constructor(
        dismissOnBackPress: Boolean = true,
        dismissOnClickOutside: Boolean = true,
        securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    ) : this(
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        securePolicy = securePolicy,
        usePlatformDefaultWidth = true,
        decorFitsSystemWindows = true
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DialogProperties) return false

        if (dismissOnBackPress != other.dismissOnBackPress) return false
        if (dismissOnClickOutside != other.dismissOnClickOutside) return false
        if (securePolicy != other.securePolicy) return false
        if (usePlatformDefaultWidth != other.usePlatformDefaultWidth) return false
        if (decorFitsSystemWindows != other.decorFitsSystemWindows) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dismissOnBackPress.hashCode()
        result = 31 * result + dismissOnClickOutside.hashCode()
        result = 31 * result + securePolicy.hashCode()
        result = 31 * result + usePlatformDefaultWidth.hashCode()
        result = 31 * result + decorFitsSystemWindows.hashCode()
        return result
    }
}

/**
 * Policy on setting [WindowManager.LayoutParams.FLAG_SECURE] on a window.
 */
enum class SecureFlagPolicy {
    /**
     * Inherit [WindowManager.LayoutParams.FLAG_SECURE] from the parent window and pass it on the
     * window that is using this policy.
     */
    Inherit,

    /**
     * Forces [WindowManager.LayoutParams.FLAG_SECURE] to be set on the window that is using this
     * policy.
     */
    SecureOn,
    /**
     * No [WindowManager.LayoutParams.FLAG_SECURE] will be set on the window that is using this
     * policy.
     */
    SecureOff
}

internal fun SecureFlagPolicy.shouldApplySecureFlag(isSecureFlagSetOnParent: Boolean): Boolean {
    return when (this) {
        SecureFlagPolicy.SecureOff -> false
        SecureFlagPolicy.SecureOn -> true
        SecureFlagPolicy.Inherit -> isSecureFlagSetOnParent
    }
}