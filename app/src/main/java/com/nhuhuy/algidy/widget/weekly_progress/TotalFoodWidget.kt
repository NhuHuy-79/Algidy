package com.nhuhuy.algidy.widget.weekly_progress

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.fillMaxSize
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import com.nhuhuy.algidy.MainActivity
import com.nhuhuy.algidy.feature.settings.data.WidgetExceptionLogger
import com.nhuhuy.algidy.widget.model.FreshnessStatisticModel
import com.nhuhuy.algidy.widget.model.fakeFreshnessStats
import com.nhuhuy.algidy.widget.model.toFreshnessStats
import com.nhuhuy.algidy.widget.usecase.GetFoodsUseCase
import com.nhuhuy.algidy.widget.utils.ErrorStateWidget
import com.nhuhuy.algidy.widget.utils.WidgetColorScheme
import com.nhuhuy.algidy.widget.utils.WidgetColors
import com.nhuhuy.algidy.widget.utils.toColorProvider
import com.nhuhuy.algidy.widget.weekly_progress.component.TotalFoodContent
import com.nhuhuy.algidy.widget.weekly_progress.component.TotalFoodTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TotalFoodWidget : GlanceAppWidget(), KoinComponent {
    private val getAllFoodsUseCase: GetFoodsUseCase by inject()
    private val widgetExceptionLogger: WidgetExceptionLogger by inject()
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        try {
            val freshnessStats = getAllFoodsUseCase().toFreshnessStats()
            provideContent {
                GlanceTheme(WidgetColorScheme.colors) {
                    TotalFoodWidget(freshnessStatisticModel = freshnessStats)
                }
            }
        } catch (e: Exception) {
            widgetExceptionLogger.log(e, "$id")
            provideContent {
                ErrorStateWidget()
            }
        }
    }

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        val fakeStats = fakeFreshnessStats
        provideContent {
            TotalFoodWidget(freshnessStatisticModel = fakeStats)
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            widgetExceptionLogger.log(throwable, "$glanceId")
        }
        super.onCompositionError(
            context,
            glanceId,
            appWidgetId,
            throwable
        )
    }
}

@Composable
private fun TotalFoodWidget(
    freshnessStatisticModel: FreshnessStatisticModel,
) {
    Scaffold(
        modifier = GlanceModifier.clickable(
            onClick = actionStartActivity<MainActivity>()
        ),
        backgroundColor = WidgetColors.BACKGROUND.toColorProvider(),
        titleBar = { TotalFoodTopBar() }
    ) {
        TotalFoodContent(
            modifier = GlanceModifier.fillMaxSize(),
            freshnessStatisticModel = freshnessStatisticModel
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Composable
@Preview(widthDp = 360, heightDp = 240)
fun PreviewTotalFoodWidget() {
    GlanceTheme(WidgetColorScheme.colors) {
        androidx.glance.layout.Box(
            modifier = GlanceModifier.fillMaxSize()
                .background(WidgetColorScheme.colors.secondary),
            contentAlignment = androidx.glance.layout.Alignment.Center
        ) {
            TotalFoodWidget(freshnessStatisticModel = fakeFreshnessStats)
        }
    }
}