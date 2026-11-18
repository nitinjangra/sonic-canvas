# Changelog

All notable changes to SonicCanvas will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v1.0.0/spec/v1.0.0).

---

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
  - Real-time audio frequency spectrum visualizer (`WaveformIcon`) with dynamic animations.
- **Playback Modes & Controls**:
  - Advanced Repeat modes supporting **Off**, **Loop (Repeat All)**, and **Repeat 1** (`RepeatOne`), with dynamic icon states.
  - Shuffle and queue management.
- **Smart Library Management & Utilities**:
  - MediaStore audio scanner with hidden folder exclusion and customizable track duration filters.
  - Custom playlists, Favorites, Recently Played, and Most Played auto-playlists.
  - Sleep timer with a revolving animated hourglass indicator.
- **Production Reliability & Privacy**:
  - 100% offline-first privacy architecture with zero network tracking or cloud telemetry.
  - Local crash logging (`LocalLogger`) capturing unhandled exceptions and stacktraces to `[FilesDir]/SonicCanvas_Debug_Log.txt`.
  - Automatic crash-on-restart recovery notifications.
- **Testing & Tooling**:
  - Comprehensive unit test suite for LRC parsing and playlist flows covering both positive and negative test scenarios.
  - Production release configuration with R8 code shrinking, resource shrinking, and custom ProGuard keep rules.
