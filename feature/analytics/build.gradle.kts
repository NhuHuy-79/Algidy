plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.analytics"
}

dependencies{
    implementation(libs.androidx.material3.android)
    implementation(libs.compose.charts.v0110)

    //Vico chart library
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.glance)
}
