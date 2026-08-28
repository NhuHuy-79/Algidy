package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingSliderType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.roundToInt

@Composable
fun SliderItem(
    currentValue: Int,
    itemPosition: ItemPosition = ItemPosition.SINGLE,
    settingItem: SettingSliderType,
    onSliderChange: (SettingSliderType, value: Int) -> Unit
) {
    val algidyIcons = AlgidyIcons.Settings
    val headlineText = when (settingItem) {
        SettingSliderType.EXPIRY_WARNING_THRESHOLD -> pluralStringResource(
            R.plurals.notification_days_before, count = currentValue, currentValue
        )

        SettingSliderType.EXPIRED_DELETE_THRESHOLD -> if (currentValue == 0) stringResource(R.string.settings_never_delete_expired_food) else
            pluralStringResource(
                R.plurals.settings_delete_threshold,
                count = currentValue,
                currentValue
            )
    }


    val icon = when (settingItem) {
        SettingSliderType.EXPIRY_WARNING_THRESHOLD -> algidyIcons.ExpiryReminder
        SettingSliderType.EXPIRED_DELETE_THRESHOLD -> algidyIcons.DeleteReminder
    }

    val values = when (settingItem) {
        SettingSliderType.EXPIRY_WARNING_THRESHOLD -> listOf(1, 3, 5, 7)
        SettingSliderType.EXPIRED_DELETE_THRESHOLD -> listOf(0, 1, 2, 3, 4)
    }

    SliderContent(
        modifier = Modifier.clip(itemPosition.toVerticalSegmentedShape()),
        icon = icon.toImageVector(),
        headlineText = headlineText,
        selectedValue = currentValue,
        values = values.toImmutableList(),
        onSliderChange = { value ->
            onSliderChange(
                settingItem,
                value
            )
        }
    )
}

@Composable
private fun SliderContent(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    headlineText: String,
    selectedValue: Int,
    values: ImmutableList<Int>,
    steps: Int = values.size - 2,
    onSliderChange: (Int) -> Unit,
) {
    ListItem(
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        headlineContent = {
            Text(
                text = headlineText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Slider(
                modifier = Modifier
                    .height(36.dp)
                    .padding(vertical = 8.dp),
                value = selectedValue.toFloat(),
                onValueChange = {
                    onSliderChange(it.roundToInt())
                },
                valueRange = values.first().toFloat()..values.last().toFloat(),
                steps = steps
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
    )
}
