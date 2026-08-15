package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.designsystem.component.AppButton
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailBottomSheet(
    foodItem: FoodCardUiModel,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit = {},
    onWastedClick: () -> Unit,
    onConsumedClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val localSpacing = LocalAlgidySpacing.current
    AppBottomSheet(
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailMainContent(
                modifier = Modifier,
                categoryUiModel = foodItem.categoryUiModel,
                foodItem = foodItem,
                onEditClick = onEditClick
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(localSpacing.medium)
            ) {
                AppButton(
                    onClick = onWastedClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    icon = ImageVector.vectorResource(R.drawable.ic_delete),
                    text = stringResource(R.string.detail_fab_mark_as_wasted),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.errorContainer,
                        contentColor = scheme.onErrorContainer
                    ),
                )

                AppButton(
                    onClick = onConsumedClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    icon = ImageVector.vectorResource(com.nhuhuy.algidy.core.designsystem.R.drawable.ic_fork_spoon),
                    text = stringResource(R.string.detail_fab_consume_this),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.primary,
                        contentColor = scheme.onPrimary
                    ),
                )
            }
        }
    }
}
