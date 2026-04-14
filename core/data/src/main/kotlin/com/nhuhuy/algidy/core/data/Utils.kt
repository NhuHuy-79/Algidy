package com.nhuhuy.algidy.core.data


import com.nhuhuy.algidy.core.model.ValidationResult

fun ValidationResult.toErrorMessage(): Int? {
    return when (this) {
        ValidationResult.SUCCESS -> null
        ValidationResult.EMPTY_FIELD -> (R.string.err_empty_field)
        ValidationResult.INVALID_NUMBER -> (R.string.err_invalid_number)
        ValidationResult.NEGATIVE_VALUE -> (R.string.err_negative_value)
        ValidationResult.PAST_DATE -> (R.string.err_past_date)
        ValidationResult.TEXT_TOO_SHORT -> (R.string.err_text_too_short)
        ValidationResult.IDLE -> null
    }
}