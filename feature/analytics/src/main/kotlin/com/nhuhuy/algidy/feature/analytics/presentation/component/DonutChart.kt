package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DonutChart(
    progress: Float,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.error,
    strokeWidth: Dp = 16.dp,
    animationDuration: Int = 1000,
    centerContent: @Composable ColumnScope.() -> Unit = {}
) {
    val sweepAngle = progress * 360f

    // Animation cho tiến độ
    val animatedSweepAngle by animateFloatAsState(
        targetValue = sweepAngle,
        animationSpec = tween(durationMillis = animationDuration),
        label = "donut_progress"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()

            drawArc(
                color = secondaryColor,
                startAngle = -90f + animatedSweepAngle,
                sweepAngle = 360f - animatedSweepAngle,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = animatedSweepAngle,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            centerContent()
        }
    }
}

@Composable
fun WaterPieChart(
    modifier: Modifier = Modifier, // Thêm default value để an toàn
) {
    Card(
        modifier = modifier
            .fillMaxWidth() // Đảm bảo Card chiếm hết chiều ngang
            .wrapContentHeight() // Chiều cao tự nhảy theo nội dung
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp) // Tăng padding chuẩn Expressive
                .fillMaxWidth()
        ) {
            Text(
                text = "Overall Rescue Rate",
                style = MaterialTheme.typography.titleLarge, // Dùng titleLarge cho vừa phải
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp) // Tạo khoảng cách giữa Chart và Stats
            ) {
                // 1. FIX: Cấp kích thước cụ thể cho Donut Chart
                DonutChart(
                    progress = 0.75f,
                    modifier = Modifier.size(100.dp), // Bắt buộc phải có size ở đy
                    primaryColor = MaterialTheme.colorScheme.primary,
                    secondaryColor = MaterialTheme.colorScheme.error,
                    strokeWidth = 12.dp,
                ) {
                    Text(
                        text = "${(0.75f * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Black
                    )
                }

                // 2. FIX: Column này cần chiếm phần không gian còn lại của Row
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp), // Ép chiều cao bằng với DonutChart để weight(1f) bên dưới có tác dụng
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatQuickCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        count = "142",
                        label = "Consumed",
                        icon = Icons.Outlined.Restaurant,
                        containerColor = MaterialTheme.colorScheme.primary, // Dùng Container cho dịu
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                    StatQuickCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        count = "26",
                        label = "Wasted",
                        icon = Icons.Outlined.Delete,
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}