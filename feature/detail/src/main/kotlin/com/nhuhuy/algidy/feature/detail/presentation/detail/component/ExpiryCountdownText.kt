package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLocale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun PulsingCountdownText(
    expiryDate: Long,
    modifier: Modifier = Modifier
) {
    // 1. Lưu thời gian hiện tại
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // 2. Khởi tạo tỷ lệ scale mặc định là 1f (100% kích thước) cho hiệu ứng Pulse
    val scale = remember { Animatable(1f) }

    // 3. Vòng lặp đếm ngược và chạy animation
    LaunchedEffect(expiryDate) {
        while (true) {
            currentTime = System.currentTimeMillis()

            // Kích hoạt hiệu ứng "nhịp đập" ngầm để không block vòng lặp thời gian
            launch {
                // Thu nhỏ nhẹ xuống 95% (0.95f) trong 100ms
                scale.animateTo(
                    targetValue = 0.95f,
                    animationSpec = tween(durationMillis = 100)
                )
                // Nảy mượt mà về lại 100% (1f) bằng hiệu ứng lò xo (Spring)
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            // Đợi đúng 1 giây rồi lặp lại
            delay(1000L)
        }
    }

    // 4. Tính toán thời gian còn lại
    val remainingMillis = expiryDate - currentTime

    // 5. Build chuỗi text hiển thị với định dạng 00 (Format String)
    val displayText = if (remainingMillis <= 0) {
        "00d: 00h: 00m: 00s"
    } else {
        val days = TimeUnit.MILLISECONDS.toDays(remainingMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(remainingMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60

        String.format(
            LocalLocale.current.platformLocale,
            "%02dd: %02dh: %02dm: %02ds",
            days, hours, minutes, seconds
        )
    }

    Text(
        text = displayText,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}
