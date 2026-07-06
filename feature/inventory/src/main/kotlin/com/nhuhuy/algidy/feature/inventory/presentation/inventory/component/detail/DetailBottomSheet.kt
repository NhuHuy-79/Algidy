package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailBottomSheet(
    foodItem: FoodItem,
    categoryUiModel: CategoryUiModel,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit = {},
    onWastedClick: () -> Unit,
    onConsumedClick: () -> Unit,
) {
    AppBottomSheet(
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailMainContent(
                categoryUiModel = categoryUiModel,
                foodItem = foodItem
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(32.dp),
                    onClick = onConsumedClick,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AlgidyTheme.extendedColors.onConsumed,
                        containerColor = AlgidyTheme.extendedColors.consumed
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Restaurant,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.detail_fab_consume_this),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                SplitButtonLayout(
                    spacing = 4.dp,
                    leadingButton = {
                        Button(
                            onClick = onEditClick,
                            shape = RoundedCornerShape(
                                topEnd = 8.dp,
                                bottomEnd = 8.dp,
                                topStart = 32.dp,
                                bottomStart = 32.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null
                            )
                        }
                    },
                    trailingButton = {
                        FilledTonalIconButton(
                            shape = RoundedCornerShape(
                                topStart = 8.dp,
                                bottomStart = 8.dp,
                                topEnd = 32.dp,
                                bottomEnd = 32.dp
                            ),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = AlgidyTheme.extendedColors.onWasted,
                                containerColor = AlgidyTheme.extendedColors.wasted
                            ),
                            onClick = onWastedClick
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteForever,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    }
}
