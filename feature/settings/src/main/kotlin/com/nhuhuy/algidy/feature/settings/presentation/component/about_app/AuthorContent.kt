@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.nhuhuy.algidy.feature.settings.presentation.component.about_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.SquaredIconButton
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.settings.R
import com.nhuhuy.algidy.feature.settings.data.AuthorConstant

@Composable
fun AuthorContent(
    itemPosition: ItemPosition,
    modifier: Modifier = Modifier,
    onGithubClick: () -> Unit,
    onEmailClick: () -> Unit,
    onLinkedlnClick: () -> Unit
) {
    AuthorContent(
        modifier = modifier,
        onGithubClick = onGithubClick,
        onEmailClick = onEmailClick,
        onLinkedlnClick = onLinkedlnClick,
        shape = itemPosition.toVerticalSegmentedShape()
    )
}

@Composable
private fun AuthorContent(
    modifier: Modifier = Modifier,
    onGithubClick: () -> Unit,
    onEmailClick: () -> Unit,
    onLinkedlnClick: () -> Unit,
    shape: Shape,
) {
    ListItem(
        shapes = ListItemDefaults.shapes(
            shape = shape
        ),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier,
        onClick = {},
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(shape = MaterialShapes.Square.toShape()),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.avatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        },
        overlineContent = {
            Text(
                text = AuthorConstant.NAME,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = {
            ButtonsContent(
                modifier = Modifier.fillMaxWidth(),
                onGithubClick = onGithubClick,
                onEmailClick = onEmailClick,
                onLinkedlnClick = onLinkedlnClick
            )
        },
    ) {
        Text(
            text = AuthorConstant.CONTENT,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ButtonsContent(
    modifier: Modifier = Modifier,
    onGithubClick: () -> Unit,
    onEmailClick: () -> Unit,
    onLinkedlnClick: () -> Unit,
) {
    val algidyIcons = AlgidyIcons.Settings
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End)
    ) {
        SquaredIconButton(
            onClick = onGithubClick,
            icon = algidyIcons.Github.toImageVector()
        )

        SquaredIconButton(
            onClick = onEmailClick,
            icon = algidyIcons.Email.toImageVector()
        )


        SquaredIconButton(
            onClick = onLinkedlnClick,
            icon = algidyIcons.LinkedIn.toImageVector()
        )
    }
}

