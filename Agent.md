# AGENTS Guidelines for This Repository

This repository contains an Android application project. When working on the project interactively
with an AI coding agent, please follow the guidelines below to ensure architectural consistency,
maximum performance, and a smooth development experience.

## 1. Project Specifications

- **Minimum SDK:** 24 (or defined by project)
- **Target SDK:** 34
- **Language:** Kotlin (1.9+)
- **Build System:** Gradle (Kotlin DSL preferred)

## 2. Architecture & Design Patterns

We follow the official Android Architecture Guidelines:

- **Presentation Layer:** StateFlow/SharedFlow in ViewModels. UI defined in Jetpack Compose (or XML
  if legacy). Unidirectional Data Flow (UDF).
- **Domain Layer:** Optional UseCases for complex business logic.
- **Data Layer:** Repository pattern to abstract data sources (Room for local, Retrofit for remote).
- **Dependency Injection:** Hilt/Dagger (preferred) or Koin.

## 3. Asynchronous Programming

- **Concurrency:** Kotlin Coroutines exclusively. Avoid RxJava for new code (migrate if possible).
- **Dispatchers:** Inject Dispatchers (don't hardcode `Dispatchers.IO`) to allow testing.

## 4. UI Framework

- **Jetpack Compose:** Default choice for all new features. Follow Compose best practices (state
  hoisting, modifiers, no side-effects in composables).
- **Navigation:** Jetpack Navigation Compose.

## 5. Testing Philosophy

- **Unit Tests:** JUnit4/JUnit5, MockK for mocking, Turbine for Flow testing.
- **UI Tests:** Compose Test Rule for UI components, Espresso for legacy XML.
- Prefer testing ViewModel state emission over testing implementation details.

## 6. External Documentation

- When asked to implement a functionality that you are not sure of, refer to the
  official [Android Developer Documentation](https://developer.android.com)
  or [Kotlin Documentation](https://kotlinlang.org) for additional context and best practices.

## 7. Useful Agent Skills Recap

| Skill Folder                  | Purpose                                              |
|-------------------------------|------------------------------------------------------|
| `architecture/`               | Clean architecture, ViewModels, and Data Layer.      |
| `ui/`                         | Jetpack Compose best practices, Coil, Accessibility. |
| `performance/`                | Auditing Compose and Gradle build performance.       |
| `migration/`                  | XML to Compose, RxJava to Coroutines.                |
| `testing_and_automation/`     | Unit/UI Testing setup, Emulator automation scripts.  |
| `concurrency_and_networking/` | Coroutines fixes, Retrofit networking.               |

---

Following these practices ensures that the agent-assisted development workflow stays reliable and
consistent. When in doubt, always refer to the specific agent skills provided in `.github/skills/`
for deeper task-specific context!

*Note to developers: Update this file whenever the project makes architectural shifts to ensure AI
agents stay aligned with your conventions.*