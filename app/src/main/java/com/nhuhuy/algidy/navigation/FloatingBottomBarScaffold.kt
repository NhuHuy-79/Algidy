package com.nhuhuy.algidy.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
    floatingActionButton: @Composable (() -> Unit)? = null,
    bottomBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val localSpacing = LocalAlgidySpacing.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        content()

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(localSpacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                floatingActionButton?.invoke()
            }
            bottomBar()
        }

    }
}