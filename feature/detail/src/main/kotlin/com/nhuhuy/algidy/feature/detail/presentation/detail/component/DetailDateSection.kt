package com.nhuhuy.algidy.feature.detail.presentation.detail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toRoundedCornerShape
import com.nhuhuy.algidy.toReadableDate

@Composable
fun DetailDateSection(
    modifier: Modifier = Modifier,
    purchaseDate: Long,
    expiryDate: Long
) {
    CardLayout(
        modifier = modifier.fillMaxWidth(),
        icon = Icons.Rounded.CalendarToday,
        title = "Dates"
    ) {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ItemPosition.TOP.toRoundedCornerShape(small = 8.dp)),
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.HourglassTop,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text(
                    text = "Purchase Date",
                    fontWeight = FontWeight.Medium
                )
            },
            supportingContent = {
                Text(
                    text = purchaseDate.toReadableDate()
                )
            }
        )
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ItemPosition.BOTTOM.toRoundedCornerShape(small = 8.dp)),
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.HourglassBottom,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text(
                    text = "Expiry Date",
                    fontWeight = FontWeight.Medium
                )
            },
            supportingContent = {
                Text(
                    text = expiryDate.toReadableDate()
                )
            }
        )
    }
}