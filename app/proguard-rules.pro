# Algidy Specific ProGuard Rules

# Project Models - Keep all models and serializable data
-keep class com.nhuhuy.algidy.core.model.** { *; }
-keep class com.nhuhuy.algidy.feature.**.presentation.viewmodel.** { *; }
-keep class com.nhuhuy.algidy.feature.**.domain.model.** { *; }
-keep class com.nhuhuy.algidy.feature.**.domain.usecase.** { *; }
-keep class com.nhuhuy.algidy.core.data.repository.** { *; }
-keep class com.nhuhuy.algidy.core.**.model.** { *;}
-keep class com.nhuhuy.algidy.feature.**.data.repository.** { *; }
-keep class com.nhuhuy.algidy.feature.**.presentation.model.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, EnclosingMethod, InnerClasses, Signature
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}
-keep class kotlinx.serialization.internal.EnumSerializer { *; }

# CameraX
-keep class androidx.camera.core.** { *; }
-keep class androidx.camera.camera2.** { *; }
-keep class androidx.camera.lifecycle.** { *; }
-keep class androidx.camera.view.** { *; }
-dontwarn androidx.camera.view.**
-dontwarn androidx.camera.camera2.internal.compat.params.DynamicRangeProfilesCompat

# ML Kit
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.internal.mlkit_** { *; }
-dontwarn com.google.mlkit.**

# Guava (often used with CameraX)
-dontwarn com.google.common.util.concurrent.ListenableFuture

# Koin
-keep class io.insertkoin.** { *; }
-keepclassmembers class * {
  @org.koin.core.annotation.KoinInternalApi *;
}

# Retrofit & OkHttp
-keepattributes Signature, InnerClasses, EnclosingMethod
-keep class com.nhuhuy.algidy.core.network.model.** { *;}
-keep interface com.nhuhuy.algidy.core.network.api.** { *; }
-keep class retrofit2.Converter$Factory { *; }
-keep class retrofit2.CallAdapter$Factory { *; }
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**


# Room
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity
-keep class * extends androidx.room.Dao

# Biometric
-keep class androidx.biometric.** { *; }

# Loggers (Strip logs in release)

# General optimization
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
