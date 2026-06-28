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
    api(libs.room.runtime)
    api(libs.room.ktx)
    ksp(libs.room.compiler)
    api(libs.androidx.sqlite.ktx)
}
