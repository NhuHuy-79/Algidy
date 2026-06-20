package com.nhuhuy.algidy.feature.scanner.utils

object ScannerValidator {

    fun validateBarcode(barcode: String): ScannerValidateResult {
        return when {
            barcode.isValidEan13() -> ScannerValidateResult.VALID
            else -> ScannerValidateResult.INVALID
        }
    }

    private fun String.isValidEan13(): Boolean {
        if (this.length != 13 || !this.all { it.isDigit() }) return false
        val digits = this.map { it.toString().toInt() }

        //Algorithm for check if last character is digit
        val sum = digits.take(12).mapIndexed { index, i ->
            if (index % 2 == 0) i else i * 3
        }.sum()

        val checkDigit = (10 - (sum % 10)) % 10
        return digits[12] == checkDigit
    }
}

enum class ScannerValidateResult {
    VALID, INVALID
}