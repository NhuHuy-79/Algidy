package com.nhuhuy.algidy.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.model.ValidationResult
import com.nhuhuy.algidy.core.presentation.R


fun ValidationResult.toStringRes(): Int? {
    return when (this) {
        ValidationResult.SUCCESS,
        ValidationResult.IDLE -> null

        ValidationResult.EMPTY_FIELD -> R.string.error_empty_field
        ValidationResult.INVALID_NUMBER -> R.string.error_invalid_number
        ValidationResult.NEGATIVE_VALUE -> R.string.error_negative_value
        ValidationResult.PAST_DATE -> R.string.error_past_date
        ValidationResult.TEXT_TOO_SHORT -> R.string.error_text_too_short
        ValidationResult.FUTURE_DATE -> R.string.error_future_date
        ValidationResult.INVALID_DATE_RANGE -> R.string.error_invalid_date_range
    }
}

@Composable
@ReadOnlyComposable
fun ValidationResult.asString(): String? {
    val resId = this.toStringRes()
    return if (resId != null) {
        stringResource(id = resId)
    } else {
        null
    }
}