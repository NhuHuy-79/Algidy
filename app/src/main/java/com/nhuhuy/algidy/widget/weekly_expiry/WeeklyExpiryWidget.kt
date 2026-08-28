package com.nhuhuy.algidy.widget.weekly_expiry

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.PreviewSizeMode
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import com.nhuhuy.algidy.feature.settings.data.WidgetExceptionLogger
import com.nhuhuy.algidy.widget.model.ExpiryFoodModel
import com.nhuhuy.algidy.widget.model.fakeExpiryFoodList
import com.nhuhuy.algidy.widget.model.toFoodWidgetModelList
import com.nhuhuy.algidy.widget.usecase.GetFoodsUseCase
import com.nhuhuy.algidy.widget.utils.WidgetColors
import com.nhuhuy.algidy.widget.utils.WidgetLayoutConfig
import com.nhuhuy.algidy.widget.utils.toColorProvider
import com.nhuhuy.algidy.widget.weekly_expiry.component.WeeklyExpiryLargeTopBar
import com.nhuhuy.algidy.widget.weekly_expiry.component.WeeklyExpiryMediumTopBar
import com.nhuhuy.algidy.widget.weekly_expiry.component.WeeklyExpirySmallTopBar
import com.nhuhuy.algidy.widget.weekly_expiry.component.WeeklyLargeExpiryContent
import com.nhuhuy.algidy.widget.weekly_expiry.component.WeeklyMediumExpiryContent
import com.nhuhuy.algidy.widget.weekly_expiry.component.WeeklySmallExpiryContent
import com.nhuhuy.algidy.widget.worker.CallbackScheduler
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

val foodIdKey = ActionParameters.Key<String>(CallbackScheduler.FOOD_ID)

class WeeklyExpiryWidget : GlanceAppWidget(), KoinComponent {
    private val getThisWeekFoods: GetFoodsUseCase by inject()
    private val widgetExceptionLogger: WidgetExceptionLogger by inject()

    override val sizeMode: SizeMode
        get() = WidgetLayoutConfig.defaultSizeMode

    override val previewSizeMode: PreviewSizeMode
        get() = WidgetLayoutConfig.defaultSizeMode

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val expiryFoodModels = getThisWeekFoods.getThisWeek().toFoodWidgetModelList()
        provideContent {
            val size = LocalSize.current
            Timber.tag("AlgidyWidget").d("size=${size.width} x ${size.height}")

            when (WidgetLayoutConfig.getModeForSize(size)) {
                WidgetLayoutConfig.WidgetMode.COMPACT -> WeekExpirySmallWidget(expiryFoodModels)
                WidgetLayoutConfig.WidgetMode.MEDIUM -> WeekExpiryMediumWidget(
                    expiryFoodModels = expiryFoodModels,
                    onConsume = {}
                )

                WidgetLayoutConfig.WidgetMode.EXPANDED -> WeekExpiryLargeWidget(
                    expiryFoodModels = expiryFoodModels,
                    onConsume = {}
                )
            }
        }
    }

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        val expiryFoodModels = fakeExpiryFoodList
        provideContent {
            val size = LocalSize.current
            GlanceTheme {
                when (WidgetLayoutConfig.getModeForSize(size)) {
                    WidgetLayoutConfig.WidgetMode.COMPACT -> WeekExpirySmallWidget(expiryFoodModels)
                    WidgetLayoutConfig.WidgetMode.MEDIUM -> WeekExpiryMediumWidget(
                        expiryFoodModels = expiryFoodModels,
                        onConsume = {}
                    )

                    WidgetLayoutConfig.WidgetMode.EXPANDED -> WeekExpiryLargeWidget(
                        expiryFoodModels = expiryFoodModels,
                        onConsume = {}
                    )
                }
            }
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
fun WeekExpirySmallWidget(
    expiryFoodModels: ImmutableList<ExpiryFoodModel>,
) {
    Scaffold(
        modifier = GlanceModifier.fillMaxSize(),
        titleBar = {
            WeeklyExpirySmallTopBar(
                modifier = GlanceModifier
            )
        },
        backgroundColor = WidgetColors.BACKGROUND.toColorProvider()
    ) {
        WeeklySmallExpiryContent(
            expiryFoodModels = expiryFoodModels,
            modifier = GlanceModifier.fillMaxWidth()
        )
    }
}

@Composable
fun WeekExpiryMediumWidget(
    expiryFoodModels: ImmutableList<ExpiryFoodModel>,
    onConsume: (id: String) -> Unit
) {
    Scaffold(
        modifier = GlanceModifier.fillMaxSize(),
        titleBar = {
            WeeklyExpiryMediumTopBar(
                modifier = GlanceModifier
            )
        },
        backgroundColor = WidgetColors.BACKGROUND.toColorProvider()
    ) {
        WeeklyMediumExpiryContent(
            expiryFoodModels = expiryFoodModels,
            modifier = GlanceModifier.fillMaxWidth(),
            onConsume = onConsume
        )
    }
}

@Composable
fun WeekExpiryLargeWidget(
    expiryFoodModels: ImmutableList<ExpiryFoodModel>,
    onConsume: (id: String) -> Unit
) {
    Scaffold(
        modifier = GlanceModifier.fillMaxSize(),
        titleBar = {
            WeeklyExpiryLargeTopBar(
                modifier = GlanceModifier
            )
        },
        backgroundColor = WidgetColors.BACKGROUND.toColorProvider()
    ) {
        WeeklyLargeExpiryContent(
            expiryFoodModels = expiryFoodModels,
            modifier = GlanceModifier.fillMaxWidth(),
            onConsume = onConsume
        )
    }
}