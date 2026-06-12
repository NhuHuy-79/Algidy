# Kotlinx Serialization
-keepattributes *Annotation*

# Retrofit
-keep interface com.nhuhuy.algidy.core.network.api.** { *; }
-keep class com.nhuhuy.algidy.core.network.model.** { *; }

# Biometric (nếu cần)
-keep class androidx.biometric.** { *; }

# Dontwarn
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**