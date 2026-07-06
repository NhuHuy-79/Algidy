package com.nhuhuy.algidy.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
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
import com.nhuhuy.algidy.core.designsystem.theme.onPrimaryContainerDark
import com.nhuhuy.algidy.core.designsystem.theme.onPrimaryContainerLight
import com.nhuhuy.algidy.core.designsystem.theme.onSecondaryContainerDark
import com.nhuhuy.algidy.core.designsystem.theme.onSecondaryContainerLight
import com.nhuhuy.algidy.core.designsystem.theme.onSurfaceDark
import com.nhuhuy.algidy.core.designsystem.theme.onSurfaceLight
import com.nhuhuy.algidy.core.designsystem.theme.secondaryContainerDark
import com.nhuhuy.algidy.core.designsystem.theme.secondaryContainerLight
import com.nhuhuy.algidy.core.designsystem.theme.surfaceContainerDark
import com.nhuhuy.algidy.core.designsystem.theme.surfaceContainerLight
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.widget.usecase.GetFoodsUseCase
import com.nhuhuy.algidy.widget.worker.CallbackScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

val foodIdKey = ActionParameters.Key<String>(CallbackScheduler.FOOD_ID)


class WeeklyExpiryWidget : GlanceAppWidget(), KoinComponent {
    private val getThisWeekFoods: GetFoodsUseCase by inject()

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val foods = getThisWeekFoods.getThisWeek()
        provideContent {
            WeeklyExpiryContent(foods = foods)
        }
    }
}

@Composable
fun WeeklyExpiryContent(
    foods: List<FoodItem>,
) {
    val context = LocalContext.current
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(
                day = surfaceContainerLight,
                night = surfaceContainerDark
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(com.nhuhuy.algidy.core.presentation.R.string.widget_weekly_food_title),
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = ColorProvider(
                        day = onPrimaryContainerLight,
                        night = onPrimaryContainerDark
                    )
                )
            )

            Spacer(modifier = GlanceModifier.defaultWeight())

            Image(
                provider = ImageProvider(com.nhuhuy.algidy.core.designsystem.R.drawable.ic_delete),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorProvider = ColorProvider(
                        day = onPrimaryContainerLight,
                        night = onPrimaryContainerDark
                    )
                ),
                modifier = GlanceModifier
                    .clickable(onClick = actionRunCallback<WasteAllFoodsCallback>())
            )

            Spacer(modifier = GlanceModifier.width(16.dp))

            Image(
                provider = ImageProvider(R.drawable.ic_refresh),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorProvider = ColorProvider(
                        day = onPrimaryContainerLight,
                        night = onPrimaryContainerDark
                    )
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
                itemsIndexed(
                    items = foods
                ) { index: Int, item: FoodItem ->
                    Box(
                        modifier = GlanceModifier
                            .padding(bottom = 8.dp)
                    ) {
                        WidgetFoodItem(
                            itemName = item.name,
                            itemLocalStorage = context.getString(item.location.toStringRes()),
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
                                color = ColorProvider(
                                    day = onSurfaceLight,
                                    night = onSurfaceDark
                                )
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
            .background(
                day = secondaryContainerLight,
                night = secondaryContainerDark
            )
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
                        color = ColorProvider(
                            day = onSecondaryContainerLight,
                            night = onSecondaryContainerDark
                        )
                    )
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = itemLocalStorage,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(
                            day = onSecondaryContainerLight,
                            night = onSecondaryContainerDark
                        )
                    )
                )
            }
            Spacer(modifier = GlanceModifier.defaultWeight())

            Image(
                provider = ImageProvider(R.drawable.ic_restaurant),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorProvider = ColorProvider(
                        day = onSurfaceLight,
                        night = onSurfaceDark
                    )
                ),
                modifier = GlanceModifier.clickable(onConsume)
            )
        }
    }
}