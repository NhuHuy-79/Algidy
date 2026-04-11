plugins {
    id("algidy.android.library")
    id("com.google.devtools.ksp")
    id("androidx.room")
}

android {
    namespace = "com.nhuhuy.algidy.core.database"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
