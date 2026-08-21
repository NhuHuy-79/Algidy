package com.nhuhuy.algidy.feature.food_entry.presentation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.food_entry.presentation.component.FoodEntryContent
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FoodEntryScreen(
    uiState: FoodEntryUiState,
    onAction: (FoodEntryAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.entry.name.ifBlank { stringResource(R.string.food_entry_title) },
                        fontWeight = FontWeight.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(FoodEntryAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        FoodEntryContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            state = uiState,
            onAction = onAction
        )
    }
}
