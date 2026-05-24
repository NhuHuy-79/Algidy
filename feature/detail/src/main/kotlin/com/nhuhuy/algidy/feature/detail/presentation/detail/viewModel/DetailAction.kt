package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import android.net.Uri
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

@Stable
sealed interface DetailAction : UiAction {
    sealed interface EditEntryAction : DetailAction {
        data class OnImageChange(val uri: Uri?) : EditEntryAction
    }

    data object OnDismiss : DetailAction
    data object OnWasteFabPress : DetailAction
    data object OnConsumeFabPress : DetailAction
    data object OnWastedItem : DetailAction
    data object OnConsumeItem : DetailAction
    data object OnEditItem : DetailAction
}
