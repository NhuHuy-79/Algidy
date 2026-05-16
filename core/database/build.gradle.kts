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
    api("androidx.sqlite:sqlite-ktx:2.7.0-alpha01")
}
