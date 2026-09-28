package com.nhuhuy.algidy.feature.food_entry.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.component.toUiText
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.food_entry.presentation.model.FoodTemplateUiModel
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FoodTemplateDialog(
    onDismiss: () -> Unit,
    onConfirm: (FoodTemplateUiModel) -> Unit,
    onDelete: (FoodTemplateUiModel) -> Unit,
    templates: ImmutableList<FoodTemplateUiModel>,
) {
    val localSpacing = LocalAlgidySpacing.current
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 400.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = LocalAlgidyShapes.current.large
                )
                .padding(localSpacing.large),
            verticalArrangement = Arrangement.spacedBy(localSpacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialShapes.Pill.toShape()
                    ),
                contentAlignment = Alignment.Center
            ) {
                AppIcon(
                    iconProvider = AlgidyIcons.FoodEntry.QuickPreset,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "Quick Add Templates",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )



            if (templates.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No templates found",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    itemsIndexed(
                        items = templates,
                        key = { _: Int, template: FoodTemplateUiModel -> template.id }
                    ) { index: Int, item: FoodTemplateUiModel ->
                        FoodTemplateItem(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth(),
                            item = item,
                            position = index.toItemPosition(templates.size),
                            onClick = { onConfirm(item) },
                            onDelete = { onDelete(item) }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FoodTemplateItem(
    modifier: Modifier = Modifier,
    item: FoodTemplateUiModel,
    position: ItemPosition = ItemPosition.SINGLE,
    onClick: () -> Unit,
    onDelete: () -> Unit

) {
    ListItem(
        shapes = ListItemDefaults.shapes(
            shape = position.toVerticalSegmentedShape(),
            pressedShape = LocalAlgidyShapes.current.medium
        ),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onSurface,
            supportingColor = MaterialTheme.colorScheme.onSurface,
            trailingIconColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        onClick = onClick,
        trailingContent = {
            IconButton(
                onClick = onDelete
            ) {
                AppIcon(
                    iconProvider = AlgidyIcons.FoodEntry.Delete,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
        supportingContent = {
            Text(
                modifier = Modifier.basicMarquee(),
                text = "${item.category.toUiText()} • ${stringResource(item.storageLocation.toStringRes())} • ${item.defaultExpiryDays} days",
                style = MaterialTheme.typography.labelMedium,
            )
        }
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
        )
    }
}