package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.horizontalRoundedCornerShape

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailToolbars(
    modifier: Modifier = Modifier,
    onMarkConsumed: () -> Unit,
    onMarkWasted: () -> Unit,
) {
    val consumedInteractionSource = remember { MutableInteractionSource() }
    val wastedInteractionSource = remember { MutableInteractionSource() }
    val isConsumedPressed by consumedInteractionSource.collectIsPressedAsState()
    val isWastedPressed by wastedInteractionSource.collectIsPressedAsState()

    val consumedAnimatedDp by animateDpAsState(
        targetValue = if (!isConsumedPressed) 4.dp else 32.dp
    )

    val wastedAnimatedDp by animateDpAsState(
        targetValue = if (!isWastedPressed) 4.dp else 32.dp
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onMarkConsumed,
            modifier = Modifier.weight(0.5f),
            shape = horizontalRoundedCornerShape(
                start = 32.dp,
                end = consumedAnimatedDp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = AlgidyTheme.extendedColors.consumedContainer,
                contentColor = AlgidyTheme.extendedColors.onConsumedContainer
            ),
            interactionSource = consumedInteractionSource
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Restaurant,
                    contentDescription = null
                )

                Text(
                    text = stringResource(R.string.detail_fab_consume_this)
                )
            }
        }

        FilledTonalButton(
            onClick = onMarkWasted,
            modifier = Modifier.weight(0.5f),
            shape = horizontalRoundedCornerShape(
                start = wastedAnimatedDp,
                end = 32.dp
            ),
            interactionSource = wastedInteractionSource,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = AlgidyTheme.extendedColors.wastedContainer,
                contentColor = AlgidyTheme.extendedColors.onWastedContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeleteForever,
                    contentDescription = null
                )

                Text(
                    text = stringResource(R.string.detail_fab_mark_as_wasted)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FoodActionGroup() {
    ButtonGroup(
        overflowIndicator = { menuState ->
            ButtonGroupDefaults.OverflowIndicator(menuState)
        }
    ) {
        clickableItem(
            label = "Consume",
            icon = {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null
                )
            },
            onClick = { }
        )

        clickableItem(
            label = "Edit",
            icon = {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = null
                )
            },
            onClick = { }
        )

        clickableItem(
            label = "Waste",
            icon = {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = null
                )
            },
            onClick = { }
        )
    }
}