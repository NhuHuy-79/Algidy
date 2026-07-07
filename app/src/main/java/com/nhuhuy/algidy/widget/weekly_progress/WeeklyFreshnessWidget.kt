package com.nhuhuy.algidy.widget.weekly_progress

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.nhuhuy.algidy.MainActivity
import com.nhuhuy.algidy.core.designsystem.R
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.widget.state.WeeklyFreshnessModel
import com.nhuhuy.algidy.widget.state.toWeeklyFreshnessModel
import com.nhuhuy.algidy.widget.usecase.GetFoodsUseCase
import com.nhuhuy.algidy.widget.weekly_progress.component.FreshnessColumn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class WeeklyFreshnessWidget : GlanceAppWidget(), KoinComponent {
    private val getFoodsUseCase: GetFoodsUseCase by inject()
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val weeklyFreshness = getFoodsUseCase.getThisWeek().toWeeklyFreshnessModel()
        provideContent {
            GlanceTheme {
                WeeklyFreshnessContent(weeklyFreshness = weeklyFreshness)
            }
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        Timber.e(throwable)
    }
}

@Composable
fun WeeklyFreshnessContent(
    weeklyFreshness: WeeklyFreshnessModel,
) {
    val context = LocalContext.current
    Scaffold(
        modifier = GlanceModifier.fillMaxSize().clickable(
            actionStartActivity<MainActivity>()
        ),
        backgroundColor = GlanceTheme.colors.surface,
        titleBar = {
            TitleBar(
                startIcon = ImageProvider(resId = R.drawable.ic_round_progress),
                title = context.getString(com.nhuhuy.algidy.core.presentation.R.string.widget_weekly_freshness_title),
                textColor = GlanceTheme.colors.onSurface
            )
        }
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Freshness.entries.forEach { freshness ->
                FreshnessColumn(
                    freshness = freshness,
                    count = weeklyFreshness.freshnessWithCount[freshness] ?: 0
                )

                Spacer(modifier = GlanceModifier.defaultWeight())
            }
        }
    }
}