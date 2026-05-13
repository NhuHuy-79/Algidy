plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.settings"
}

dependencies {
    implementation(project(":core:datastore"))
}
