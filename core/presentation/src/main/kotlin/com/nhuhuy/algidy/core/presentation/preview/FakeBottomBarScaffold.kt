package com.nhuhuy.algidy.core.presentation.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing

@Composable
fun FloatingBottomBarScaffold(
    modifier: Modifier = Modifier,
    applyNavigationBarsPadding: Boolean = true,
    bottomBar: @Composable () -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val localSpacing = LocalAlgidySpacing.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        content()

        floatingActionButton?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 16.dp,
                        bottom = 144.dp
                    )
            ) {
                it()
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .then(
                    if (applyNavigationBarsPadding) {
                        Modifier.navigationBarsPadding()
                    } else {
                        Modifier
                    }
                )
                .padding(
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(localSpacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bottomBar()
        }
    }
}