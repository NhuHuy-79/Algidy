# 📋 Algidy Project Roadmap & TODO

This document tracks the tasks needed to move Algidy from a development state to a production-ready
GitHub release.

## 🚀 Priority: High (Must have for Release)

### 🏗 Architecture & Stability

- [ ] **Proguard/R8 Configuration:** Define rules for Koin, Room, ML Kit, and Kotlin Serialization
  to prevent crashes in Release builds.
- [ ] **Navigation 3 Audit:** Evaluate if Navigation 3 (Alpha) is stable enough or consider a
  fallback to 2.8.5 for the master branch.
- [ ] **Global Error Handling:** Implement a consistent UI for Error states instead of raw logs.

### 🍱 Feature Completion

- [ ] **Food Category Selection:** Remove hardcoded `OTHERS` category. Add selection UI in
  `ConfirmScreen` and `FoodEntryForm`.
- [ ] **Background Notifications:** Implement `WorkManager` to scan for expiring food items daily
  and trigger notifications.
- [ ] **Improved Backup/Restore:**
    - [ ] Handle URI changes across different devices during restore.
    - [ ] Add "Clear Data" option in Settings.

### 🔐 Security

- [ ] **Biometric Lock Refinement:** Ensure the lock triggers correctly on `onTrimMemory` or when
  the app is backgrounded for more than X minutes.

---

## 🛠 Priority: Medium (Polishing)

### 📊 Analytics

- [ ] **Enhanced Charts:** Add more granular data (e.g., waste by month, cost impact estimation).
- [ ] **Export Reports:** Option to export analytics data as PDF or CSV.

### 🧪 Testing

- [ ] **Unit Tests:** Focus on `core:data` (Mappers, Repository logic) and `core:model` (
  Validators).
- [ ] **UI Tests:** Basic Espresso/Compose tests for the main flows (Adding food, Scanning).

### 🎨 UI/UX

- [ ] **Empty States:** Design and implement better "Empty" illustrations for Inventory and Search.
- [ ] **Onboarding:** A simple 3-4 screen introduction for first-time users.
- [ ] **Accessibility:** Add content descriptions to all icons and images for screen readers.

---

## 📦 Priority: Low (GitHub & DevOps)

### 🤖 CI/CD

- [ ] **GitHub Actions:**
    - [ ] Create a workflow for PR linting (ktlint).
    - [ ] Create a workflow to auto-build Debug APK on every push.

### 📄 Documentation

- [ ] **Professional README:** Add screenshots, features list, tech stack, and setup instructions.
- [ ] **License:** Add MIT or Apache 2.0 license file.
- [ ] **Contributing Guide:** How others can help with the project.

---

## ✅ Completed

- [x] Modern Multi-module architecture.
- [x] Google Fonts integration with Be Vietnam Pro.
- [x] Core Scanning logic (ML Kit + CameraX).
- [x] Initial Biometric Lock implementation.
- [x] Backward compatible localization.
- [x] Basic Analytics with Charts.
