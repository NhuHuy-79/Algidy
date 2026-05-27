# Algidy

Algidy is a smart food inventory management app built with modern Android technologies. It helps
users track their groceries, reduce food waste, and maintain freshness through barcode scanning and
intelligent analytics.

## 🚀 Features

- **Inventory Tracking:** Categorize and track food items across different storage locations (
  Fridge, Freezer, Pantry).
- **Intelligent Scanning:** Fast barcode scanning and food date extraction using Google ML Kit.
- **Freshness Analytics:** Visual insights into food consumption, waste patterns, and freshness
  status.
- **Smart Notifications:** Timely alerts for items approaching their expiry dates.
- **Secure Access:** Biometric authentication to keep your data private.

## 🛠 Tech Stack

- **UI:** Jetpack Compose (100%) with Material 3.
- **Architecture:** Clean Architecture with Multi-module setup.
- **Dependency Injection:** Koin.
- **Database:** Room with SQLite.
- **Networking:** Retrofit + OkHttp.
- **Image Loading:** Coil 3.
- **AI/ML:** Google ML Kit (Barcode, OCR, Entity Extraction).
- **Concurrency:** Kotlin Coroutines & Flow.

## 📦 Download

You can find the latest APK in the [Releases](https://github.com/YOUR_USERNAME/Algidy/releases)
section.

## 🛠 Development

### Setup

1. Clone the repository.
2. Open with Android Studio (Ladybug or newer recommended).
3. (Optional) Create `keystore.properties` in the root folder for release builds (see
   `keystore.properties.template`).

### Build Modules

- `:app`: The main application.
- `:screenshot-test`: Standalone demo app for generating marketing assets.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
