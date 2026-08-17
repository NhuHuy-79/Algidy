package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.presentation.R
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.hours

@Composable
fun WeeklyProgressCard(
    disabledBackgroundColor: Color,
    activeBackgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(calculateWeekProgress()) }
    var currentDayOfWeek by remember { mutableIntStateOf(LocalDateTime.now().dayOfWeek.value) }
    val startOfWeekDate = remember {
        val monday = LocalDateTime.now().with(DayOfWeek.MONDAY)
        val formatter = DateTimeFormatter.ofPattern("dd/MM")
        monday.format(formatter)
    }

    val localShape = LocalAlgidyShapes.current

    LaunchedEffect(Unit) {
        while (true) {
            progress = calculateWeekProgress()
            currentDayOfWeek = LocalDateTime.now().dayOfWeek.value
            delay(1.hours)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(localShape.extraLarge)
            .background(disabledBackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress)
                .background(activeBackgroundColor)
                .clip(localShape.extraLarge)
        )


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = "This week",
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(
                    R.string.analytics_weekly_card_content,
                    " $startOfWeekDate"
                ),
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee(),
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = contentColor
            )


            Text(
                text = stringResource(R.string.analytics_weekly_card, currentDayOfWeek),
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )
        }
    }
}

private fun calculateWeekProgress(): Float {
    val now = LocalDateTime.now()

    val dayOfWeek = now.dayOfWeek.value
    return dayOfWeek / 7f
}

