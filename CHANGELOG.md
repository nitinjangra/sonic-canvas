# Changelog

All notable changes to Sonixa will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v1.0.0/spec/v1.0.0).

---

## [2.1.0] - 2026-03-31

### Added
- **Interactive UI Animations**:
  - Interactive Play/Pause button on Now Playing screen featuring spring-based press scaling, elevation glow, and smooth scale-fade icon morphing.
  - Animated SONIXA logo on the startup splash screen with breathing scale animation and system dark/light theme adaptation.
- **Folder Selection & File Picker (SAF)**:
  - Custom directory picking via Storage Access Framework (`Intent.ACTION_OPEN_DOCUMENT_TREE`) on first run and in Settings.
  - Human-readable folder name and path formatting (`formatFolderNameAndPath`) for the Folders tab.
- **Scanner & Rescan Optimizations**:
  - Silent scanner cancellation returning cleanly to the Music Library page without error popups.
  - Silent error recovery logging.
- **Small Screen & Touch UX Enhancements**:
  - Horizontal scrolling without scrollbars for folder labels and equalizer presets on small screen devices.
  - Clean unnumbered audiophile fun fact notification titles.
  - Official app renaming to **Sonixa** with package update to `com.pixelpushers.sonixa`.

## [2.0.0] - 2026-03-31

### Added
- **Real Audio Streaming Engine**:
  - Connected `NowPlayingViewModel` to Media3 ExoPlayer via `MusicPlayerManager` for real audiophile audio output.
- **Apple Music-Style UI & Transitions**:
  - Fluid Apple Music-style scale-fade and slide-fade transitions on the mini player pill (`MiniPlayerPill`).
  - Custom SONIXA launcher icon with dark mode and Android 13+ Material You monochrome themed icon support.
  - Enhanced glassmorphism design with multi-stop translucency gradients and specular reflection highlights (`GlassPill`).
- **Advanced Audio Effects & Visualizer**:
  - Real-time audio frequency spectrum visualizer canvas (`VisualizerSpectrumCanvas`) on the Equalizer screen.
  - Animated waveform icon (`WaveformIcon`) with active audio playback pulsing.
  - Advanced Repeat modes supporting **Off**, **Loop (Repeat All)**, and **Repeat 1** (`RepeatOne`) with dynamic icon states.
- **Gestures & Navigation UX**:
  - Swipe-down gesture minimization on the Now Playing screen.
  - Full support for hardware back buttons and gesture navigation across all overlays and sub-screens with `BackHandler`.
  - Edge-to-edge system gesture bar padding (`navigationBarsPadding()`).
- **Interactive Controls & Haptics**:
  - Configurable haptic feedback / vibration support on playback controls with a dedicated settings toggle.
- **Reactive Playlists & Smart Library**:
  - Real-time reactive custom playlist song count synchronization.
  - Optimized app startup scanning (scans automatically only on first launch/permission grant).
- **Architecture & Reliability**:
  - Repository pattern architectural refactoring using Dagger Hilt `@Binds` for Song, Playlist, and Lyrics repositories.
  - Local crash logging (`LocalLogger`) and automatic restart recovery notifications.
  - Exponentially increasing intervals ($1, 2, 4, 8, 16, 32 \dots$ hours) for audiophile fun facts based on lifetime app usage.

## [1.0.0] - 2026-03-31

### Added
- **Core Playback Engine**:
  - High-resolution offline local music playback (FLAC, MP3, WAV).
  - Resilient foreground `MediaSessionService` (`mediaPlayback` type) ensuring uninterrupted background playback and persistent lockscreen controls via Media3.
- **Modern Jetpack Compose UI & Material 3**:
  - Gorgeous dark theme design with mint green primary accent color.
  - Glassmorphic floating navigation bars and mini player pills (`GlassPill`).
  - Animated startup splash screen featuring expanding concentric rings and bouncing equalizer bars.
- **Synced Lyrics & Cover Art Integration**:
  - Live synchronized `.lrc` and plain text lyrics with automatic active line highlighting and smooth auto-scrolling.
  - Tap-to-seek support directly from lyric lines.
  - Option to replace the album art cover with live scrolling lyrics on the Now Playing screen.
- **Audio Effects & Equalizer**:
  - 5-band frequency equalizer with vertical sliders and preset pill chips (*Flat*, *Rock*, *Pop*, *Jazz*, *Bass*).
  - Adjustable Bass Boost and Virtualizer effects with live percentage indicators.
- **Production Reliability & Privacy**:
  - 100% offline-first privacy architecture with zero network tracking or cloud telemetry.
  - Local crash logging (`LocalLogger`) capturing unhandled exceptions and stacktraces to `[FilesDir]/Sonixa_Debug_Log.txt`.
  - Automatic crash-on-restart recovery notifications.
- **Testing & Tooling**:
  - Comprehensive unit test suite for LRC parsing and playlist flows covering both positive and negative test scenarios.
  - Production release configuration with R8 code shrinking, resource shrinking, and custom ProGuard keep rules.
