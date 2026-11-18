# SonicCanvas 🎵

SonicCanvas is a high-resolution, offline-first Android music player built with **Jetpack Compose**, **Material 3**, and modern Android architecture. Designed with a gorgeous dark glassmorphic UI, SonicCanvas delivers audiophile-grade local playback with rich customization, real-time audio effects, and total privacy.

---

## ✨ Key Features

### 🎧 Audiophile Playback & Audio Effects
- **High-Resolution Support**: Flawless playback of FLAC, MP3, WAV, and high-bitrate audio files.
- **5-Band Equalizer**: Custom 5-band frequency slider control with preset chips (*Flat*, *Rock*, *Pop*, *Jazz*, *Bass*).
- **Bass Boost & Virtualizer**: Adjustable bass enhancement and virtual surround audio effects.
- **Real-Time Audio Visualizer**: Dynamic waveform spectrum visualizer (`WaveformIcon`) reflecting active audio playback.
- **Background Playback**: Resilient foreground `MediaSessionService` (`mediaPlayback` type) ensuring uninterrupted background playback and persistent notification controls.

### 📜 Live Synced Lyrics & Cover Integration
- **Synced & Plain Lyrics**: Support for `.lrc` timestamped lyrics and plain text formats.
- **Cover Art / Lyrics Toggle**: Switch between album cover artwork and live scrolling synchronized lyrics directly in the Now Playing thumbnail area.
- **Tap-to-Seek**: Tap any lyric line to instantly jump playback to that timestamp.
- **Context Editing**: Built-in editor to add, paste, or update lyrics for any track.

### ⏱️ Smart Utilities & Organization
- **Sleep Timer**: Custom timer durations with a revolving animated hourglass indicator.
- **Playlists & Auto-Playlists**: Dedicated management for custom playlists, Favorites, Recently Played, and Most Played tracks.
- **Smart Library Scanner**: MediaStore audio scanner with hidden folder exclusion and track duration filters.
- **Local Privacy First**: 100% offline playback. No cloud tracking or external telemetry.

### 🛡️ Production Reliability
- **Local Crash Logger**: Captures uncaught exceptions locally (`SonicCanvas_Debug_Log.txt`) with automatic restart recovery notifications.
- **R8 Minification & Shrinking**: Fully optimized ProGuard / R8 rules for production builds.

---

## 🏛️ Architecture & Tech Stack

- **UI**: Jetpack Compose, Material 3, Custom Canvas Art (`WaveformIcon`, `LyricsIcon`), Glassmorphism (`GlassPill`).
- **Architecture**: MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF).
- **Dependency Injection**: Dagger Hilt.
- **Concurrency**: Kotlin Coroutines & `StateFlow` (`WhileSubscribed`).
- **Local Storage**: Room Database & Jetpack DataStore preferences.
- **Media Engine**: AndroidX Media3 ExoPlayer & `MediaSessionService`.

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Ladybug / Jellyfish (or newer)
- **SDK**: Compile & Target SDK 37 (Min SDK 26)
- **JDK**: Java 11+

### Building the App
To build a debug APK:
```bash
./gradlew :app:assembleDebug
```

To build a production release App Bundle (`.aab`) with R8 minification:
```bash
./gradlew :app:bundleRelease
```

---

## 🧪 Testing

To execute unit tests (including positive and negative LRC parser and playlist flows):
```bash
./gradlew testDebugUnitTest
```

---

## 📄 License

This project is licensed under the MIT License.
