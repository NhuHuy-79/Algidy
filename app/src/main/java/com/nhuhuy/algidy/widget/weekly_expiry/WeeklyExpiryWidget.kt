package com.nhuhuy.algidy.widget.weekly_expiry

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.nhuhuy.algidy.R
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.feature.settings.data.WidgetExceptionLogger
import com.nhuhuy.algidy.widget.ErrorStateWidget
import com.nhuhuy.algidy.widget.state.FoodWidgetModel
import com.nhuhuy.algidy.widget.state.toFoodWidgetModelList
import com.nhuhuy.algidy.widget.usecase.GetFoodsUseCase
import com.nhuhuy.algidy.widget.worker.CallbackScheduler
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

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        try {
            val foods = getThisWeekFoods.getThisWeek().toFoodWidgetModelList()
            provideContent {
                WeeklyExpiryContent(foods = foods)
            }
        } catch (e: Exception) {
            widgetExceptionLogger.log(e, glanceId = "$id")
            provideContent {
                ErrorStateWidget()
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
fun WeeklyExpiryContent(
    foods: List<FoodWidgetModel>,
) {
    val context = LocalContext.current
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = context.getString(com.nhuhuy.algidy.core.presentation.R.string.widget_weekly_food_title),
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = GlanceTheme.colors.onSurface
                )
            )

            Spacer(modifier = GlanceModifier.defaultWeight())

            Image(
                provider = ImageProvider(com.nhuhuy.algidy.core.designsystem.R.drawable.ic_delete),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorProvider = GlanceTheme.colors.onPrimaryContainer
                ),
                modifier = GlanceModifier
                    .clickable(onClick = actionRunCallback<WasteAllFoodsCallback>())
            )

            Spacer(modifier = GlanceModifier.width(16.dp))

            Image(
                provider = ImageProvider(R.drawable.ic_refresh),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorProvider = GlanceTheme.colors.onPrimaryContainer
                ),
                modifier = GlanceModifier
                    .clickable(
                        onClick = actionRunCallback<RefreshWeeklyExpiryWidget>()
                    )
            )
        }

        Spacer(modifier = GlanceModifier.height(24.dp))

        LazyColumn(
            modifier = GlanceModifier.fillMaxSize(),
        ) {
            if (foods.isNotEmpty()) {
                items(
                    items = foods,
                    itemId = { it.hashCode().toLong() }
                ) { item: FoodWidgetModel ->
                    Box(
                        modifier = GlanceModifier
                            .padding(bottom = 8.dp)
                    ) {
                        WidgetFoodItem(
                            itemName = item.name,
                            itemLocalStorage = context.getString(item.storageLocation.toStringRes()),
                            onConsume = actionRunCallback<ConsumeFoodCallBack>(
                                parameters = actionParametersOf(
                                    foodIdKey to item.id
                                )
                            )

                        )
                    }
                }
            } else {
                item {
                    Box(
                        modifier = GlanceModifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = context.getString(com.nhuhuy.algidy.core.presentation.R.string.widget_weekly_empty_food),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = GlanceTheme.colors.onSurface
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WidgetFoodItem(
    itemName: String,
    itemLocalStorage: String,
    onConsume: Action
) {
    Box(
        modifier = GlanceModifier.fillMaxWidth()
            .cornerRadius(8.dp)
            .background(GlanceTheme.colors.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = GlanceModifier.height(16.dp))
            Column {
                Text(
                    text = itemName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlanceTheme.colors.onSecondaryContainer
                    )
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = itemLocalStorage,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlanceTheme.colors.onSecondaryContainer
                    )
                )
            }
            Spacer(modifier = GlanceModifier.defaultWeight())

            Image(
                provider = ImageProvider(R.drawable.ic_restaurant),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorProvider = GlanceTheme.colors.onSurface
                ),
                modifier = GlanceModifier.clickable(onConsume)
            )
        }
    }
}