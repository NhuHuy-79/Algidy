package com.nhuhuy.algidy.feature.food_entry.presentation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.PhotoPickerContainer
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.food_entry.presentation.component.FoodEntryForm
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryError
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FoodEntryScreen(
    uiState: FoodEntryUiState,
    errorState: FoodEntryError,
    onAction: (FoodEntryAction) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.name.ifBlank { stringResource(R.string.food_entry_title) },
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
                actions = {
                    PhotoPickerContainer(
                        onImagePicked = { uri ->
                            uri?.let {
                                onAction(FoodEntryAction.OnImagePick(uri))
                            }
                        }
                    ) { launcher ->
                        IconButton(onClick = launcher) {
                            Icon(
                                imageVector = Icons.Outlined.AddPhotoAlternate,
                                contentDescription = stringResource(R.string.detail_pick_photo)
                            )
                        }
                    }
                },
            )
        }
    ) { innerPadding ->
        FoodEntryForm(
            entryState = uiState,
            errorState = errorState,
            onAction = onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .imePadding()
                .consumeWindowInsets(WindowInsets.ime)
                .verticalScroll(rememberScrollState())

        )
    }
}
