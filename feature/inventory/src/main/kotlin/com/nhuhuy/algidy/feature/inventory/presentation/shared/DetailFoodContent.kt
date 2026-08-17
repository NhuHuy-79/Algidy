package com.nhuhuy.algidy.feature.inventory.presentation.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel

@Composable
fun DetailFoodContent(
    foodUiModel: FoodUiModel,
    modifier: Modifier = Modifier,
) {
    MaterialTheme.colorScheme
    val localShape = LocalAlgidyShapes.current
    LocalAlgidySpacing.current

    FoodImageCard(
        imageUri = foodUiModel.imageUri,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(localShape.large)
    )
}