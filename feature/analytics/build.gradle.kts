plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.analytics"
}

dependencies{
    implementation(libs.androidx.material3.android)
    implementation(libs.chart)
    implementation("io.github.ehsannarmani:compose-charts:0.1.10")

}
