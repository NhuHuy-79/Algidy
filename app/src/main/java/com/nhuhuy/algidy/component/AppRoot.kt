package com.nhuhuy.algidy.component

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.navigation.AppGraph

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppRoot(
    showContent: Boolean
) = BoxLayout {
    val blurRadius by animateFloatAsState(
        targetValue = if (!showContent) 30f else 0f,
        animationSpec = tween(durationMillis = 500),

        label = "BlurAnimation"
    )
    AppGraph()
    if (!showContent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    if (blurRadius > 0f) {
                        renderEffect = RenderEffect.createBlurEffect(
                            blurRadius,
                            blurRadius,
                            Shader.TileMode.CLAMP
                        ).asComposeRenderEffect()
                    }
                }
                .background(Color.Black.copy(alpha = 0.2f))
        )
    }
}
