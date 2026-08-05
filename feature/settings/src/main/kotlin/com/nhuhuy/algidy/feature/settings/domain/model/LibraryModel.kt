package com.nhuhuy.algidy.feature.settings.domain.model

data class LibraryModel(
    val id: String = "",
    val name: String = "",
    val author: String = "",
    val licenses: List<String> = emptyList(),
    val versionName: String = ""
)
