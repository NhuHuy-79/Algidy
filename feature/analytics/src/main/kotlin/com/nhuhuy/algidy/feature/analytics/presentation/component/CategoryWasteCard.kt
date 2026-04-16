package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme

// Model dữ liệu mẫu cho mỗi hạng mục
data class WastedCategory(
    val name: String,
    val weight: String,
    val progress: Float // Giá trị từ 0.0f đến 1.0f
)

@Composable
fun CategoryWasteCard(
    modifier: Modifier = Modifier,
    categories: List<WastedCategory> = emptyList()
) {
    // 1. Sử dụng ElevatedCard với bo góc 28.dp (Expressive Style)
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title của Card - Dùng Poppins ExtraBold
            Text(
                text = "Waste by Category",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Danh sách các hạng mục lãng phí
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                categories.forEach { category ->
                    WastedCategoryItem(category)
                }
            }
        }
    }
}

@Composable
private fun WastedCategoryItem(category: WastedCategory) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold // Poppins Bold
            )
            Text(
                text = category.weight,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LinearProgressIndicator(
            progress = { category.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = MaterialTheme.colorScheme.tertiary,
            trackColor = MaterialTheme.colorScheme.tertiaryContainer,
            strokeCap = StrokeCap.Round
        )
    }
}

// --- PHẦN PREVIEW ---

@Preview(showBackground = true)
@Composable
private fun CategoryWasteCardPreview() {
    val sampleData = listOf(
        WastedCategory("Vegetables", "12.4kg", 0.7f),
        WastedCategory("Dairy", "8.2kg", 0.45f),
        WastedCategory("Fruits", "5.1kg", 0.25f)
    )

    AlgidyTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CategoryWasteCard(categories = sampleData)
        }
    }
}