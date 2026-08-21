plugins {
    id("algidy.android.feature")
}

android {
    namespace = "com.nhuhuy.algidy.feature.inventory"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":feature:food-entry"))
}
