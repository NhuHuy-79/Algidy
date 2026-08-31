package com.nhuhuy.algidy.core.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.backgroundLight
import com.nhuhuy.algidy.core.designsystem.theme.onPrimaryContainerLight
import com.nhuhuy.algidy.core.designsystem.theme.primaryContainerLight

@Composable
fun StoreMarketingScreen(
    headlineLines: List<String>,
    backgroundColor: Color = primaryContainerLight,
    textColor: Color = onPrimaryContainerLight,
    screenColor: Color = backgroundLight,
    bottomBarVisible: Boolean = true,
    fabContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.75f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 56.dp,
                    start = 48.dp,
                    end = 48.dp
                )
        ) {
            // Headline
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                headlineLines.forEach { line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(48.dp)
            )

            // Phone viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds(),
                contentAlignment = Alignment.TopCenter
            ) {
                PhoneFrame(
                    backgroundColor = screenColor,
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp)
                    ) {
                        if (bottomBarVisible) {
                            FloatingBottomBarScaffold(
                                applyNavigationBarsPadding = false,
                                bottomBar = {
                                    FakeBottomFloatingBar()
                                },
                                floatingActionButton = fabContent
                            ) {
                                content()
                            }
                        } else {
                            content()
                        }
                    }
                }
            }
        }
    }
}