plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.analytics"
}

dependencies{
    implementation(libs.androidx.material3.android)
    implementation(libs.compose.charts.v0110)

}
