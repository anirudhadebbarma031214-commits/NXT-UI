# NXT UI — Android TV Launcher

Kotlin / Jetpack Compose Android TV home launcher for API 26+.

## Build
Open in Android Studio with JDK 17 and Android SDK 35, sync Gradle, then run `gradle :app:assembleDebug`.
For signed release use Android Studio Build > Generate Signed App Bundle / APK and keep your keystore private. `assembleRelease` without signing configuration creates an unsigned release APK.

## Features
DPAD focus highlights, app drawer, favorites dock, session-based recent launch list, standby clock, free storage and RAM widgets, system settings shortcuts.

## Android restrictions
Third-party apps cannot force-stop arbitrary packages. Recent list is NXT UI launch history only. HDMI switching requires OEM APIs. Standby works while launcher is foreground, not as a system DreamService. Weather requires a separately configured provider and is not included. Blur is simulated with translucent surfaces to support older TVs.
