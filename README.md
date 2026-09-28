# carlos-android

Android mobile client for **Carlos ERP** — Kotlin, Jetpack Compose (Material 3),
MVVM, Hilt, Retrofit, Room.

## Tech stack

| Concern            | Choice                                             |
| ------------------ | -------------------------------------------------- |
| Language           | Kotlin 2.1                                         |
| UI                 | Jetpack Compose + Material 3                        |
| Architecture       | MVVM + clean layering (presentation/domain/data)   |
| DI                 | Hilt                                               |
| Networking         | Retrofit + OkHttp + kotlinx.serialization          |
| Local storage      | Room (offline-first preparation)                   |
| Async              | Kotlin Coroutines                                  |
| Build              | Gradle (Kotlin DSL) + version catalog              |
| Code quality       | Detekt + KtLint                                    |
| Min / Target SDK   | 26 / 35                                             |

## Project structure

```
carlos-android/
├── build.gradle.kts            # Root build (plugins apply false)
├── settings.gradle.kts         # Modules + repositories
├── gradle.properties
├── gradle/libs.versions.toml   # Version catalog (single source of versions)
├── config/detekt/detekt.yml    # Detekt ruleset
└── app/
    ├── build.gradle.kts        # App module: plugins + dependencies
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/carloserp/android/
        │   ├── CarlosApp.kt            # @HiltAndroidApp Application
        │   ├── core/                   # Cross-cutting utils (dispatchers, result, net)
        │   ├── domain/                 # Models, repository interfaces, use cases
        │   ├── data/                   # Retrofit services, Room DAOs, repo impls
        │   └── presentation/           # Activities, Compose screens, theme, ViewModels
        │       ├── MainActivity.kt
        │       └── theme/              # Color / Type / Theme (Material 3)
        └── res/                        # strings, colors, themes, adaptive icon
```

## Configuration

- **API base URL** is exposed as `BuildConfig.API_BASE_URL` (set per build type in
  `app/build.gradle.kts`; must end with `/`). It currently points at the TEST
  backend on Railway:
  `https://carlos-backend-production.up.railway.app/api/v1/`.
- **Firebase** (`google-services.json`) is git-ignored and provided per
  environment (added in task 51.x).

## Prerequisites & building

This scaffold was created in an environment **without the Android SDK/Gradle**,
so it has not been compiled here — the build is deferred to a machine with the
Android toolchain (Android Studio or CI). To build:

1. Open the project in **Android Studio** (Ladybug or newer), or install the
   Android SDK + a JDK 17.
2. Generate the Gradle wrapper jar/scripts once (not committed): run
   `gradle wrapper --gradle-version 8.11.1` (or let Android Studio do it on first
   sync). After that, use `./gradlew`.
3. Build / install:
   - `./gradlew assembleDebug` — build the debug APK.
   - `./gradlew installDebug` — install on a connected device/emulator.

## Code quality

- `./gradlew detekt` — static analysis (config in `config/detekt/detekt.yml`).
- `./gradlew ktlintCheck` / `./gradlew ktlintFormat` — Kotlin lint / auto-format.

## Branching

GitFlow: `master` (production) and `develop` (integration), both protected —
work on `feature/*` branches and open PRs into `develop`.
