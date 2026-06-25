package com.nhuhuy.algidy.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionFeatures(
    val versionCode: Int,
    val versionName: String,
    val features: List<AppNewFeature>,
    @SerialName("fixes")
    val fixItems: List<FixItem> = emptyList(),
)

@Serializable
data class AppNewFeature(
    val title: String,
    val description: String,
    @SerialName("icon")
    val icon: String
)

@Serializable
data class FixItem(
    val title: String,
    val icon: String,
    val description: String
)