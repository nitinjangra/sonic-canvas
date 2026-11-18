package com.example.soniccanvas.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class NotificationCategory(val displayName: String) {
    PLAYBACK_ISSUES("Playback issues"),
    FUN_FACTS("Fun facts"),
    REMINDERS("Reminders"),
    RELEASE_NOTES("Release notes"),
    SYSTEM("System")
}

data class AppNotification(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    val title: String,
    val message: String,
    val category: NotificationCategory,
    val timestamp: Long = System.currentTimeMillis()
)

@Singleton
class NotificationRepository @Inject constructor() {
    private val initialNotifications = listOf(
        AppNotification(
            title = "Welcome to SonicCanvas",
            message = "SonicCanvas v1.0 installed successfully. Enjoy high-resolution offline FLAC playback!",
            category = NotificationCategory.RELEASE_NOTES
        ),
        AppNotification(
            title = "Sleep Timer Reminder",
            message = "Use the Sleep Timer shortcut in Now Playing to automatically pause music when falling asleep.",
            category = NotificationCategory.REMINDERS
        ),
        AppNotification(
            title = "Audiophile Fact 1: FLAC Compression",
            message = "FLAC (Free Lossless Audio Codec) reduces file sizes by 50-60% without throwing away a single bit of original audio data.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 2: Dynamic Range",
            message = "24-bit audio provides 16.7 million discrete amplitude steps compared to 65,536 in 16-bit audio, offering incredible micro-detail in quiet passages.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 3: Nyquist Theorem",
            message = "Sampling audio at 44.1kHz (CD quality) perfectly reconstructs frequencies up to 22.05kHz, covering the absolute upper limit of human hearing.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 4: Tube Amplifiers",
            message = "Vacuum tube amplifiers introduce warm even-order harmonic distortion, which the human ear perceives as pleasing and musically rich.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 5: Planar Magnetic Drivers",
            message = "Planar magnetic headphones use an ultra-thin diaphragm suspended between magnets, delivering lightning-fast transient response and ultra-low distortion.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 6: Room Acoustics",
            message = "Room reflections and standing waves account for up to 50% of the sound quality experienced in a listening room, often mattering more than speaker price.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 7: Balanced Audio",
            message = "Balanced audio cables use differential signaling to cancel out electromagnetic interference picked up over long cable runs.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 8: Bit Depth & Dynamic Range",
            message = "Every increase of 1 bit in digital audio depth adds 6 dB of dynamic range, which is why 24-bit audio is prized by mastering engineers.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 9: Moving Coil Speakers",
            message = "The moving coil loudspeaker driver, invented by Rice and Kellogg in 1925, remains the foundational acoustic design used in 99% of modern speakers.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 10: Custom IEMs",
            message = "High-end custom In-Ear Monitors (IEMs) use precision 3D-printed shells molded directly from impressions of the user's ear canals for absolute isolation.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 11: Audio Jitter",
            message = "Jitter is timing error in digital clock signals that can cause subtle phase distortion and harshness in digital-to-analog converters (DACs).",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 12: Active Crossovers & DSP",
            message = "Digital Signal Processing (DSP) and active crossovers allow modern wireless speakers to achieve flat frequency responses previously impossible with passive analog filters.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 13: Bone Conduction Audio",
            message = "Bone conduction headphones bypass the eardrum entirely, transmitting audio vibrations directly through skull bones to the cochlea.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 14: Open-Back Soundstage",
            message = "Open-back headphones allow air to pass freely through the earcups, producing a wide, natural soundstage resembling live speakers.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 15: Master Tape Bandwidth",
            message = "Master tapes recorded at 30 inches per second (IPS) on reel-to-reel tape machines possess bandwidth and warmth that engineers spend decades trying to emulate.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 16: Harman Target Curve",
            message = "The Harman Target response curve is scientifically tuned to replicate the sound of flat studio monitors interacting with an average human ear and room.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 17: Electrostatic Headphones",
            message = "Electrostatic headphones require high-voltage polarizing bias (often 500V+) to move an ultralight charged film diaphragm suspended between stators.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 18: R-2R Ladder DACs",
            message = "R-2R ladder DACs use precision resistor networks to convert digital samples directly to analog voltage without Delta-Sigma noise shaping.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 19: HRTF Spatial Audio",
            message = "HRTF (Head-Related Transfer Function) describes how the human head, torso, and pinna shape sounds differently depending on direction, enabling realistic 3D spatial audio.",
            category = NotificationCategory.FUN_FACTS
        ),
        AppNotification(
            title = "Audiophile Fact 20: Vinyl vs Digital",
            message = "Vinyl records have physical groove limitations, whereas 24-bit/96kHz digital audio captures dynamic range exceeding 144 dB, far beyond human hearing.",
            category = NotificationCategory.FUN_FACTS
        )
    )

    private val _notifications = MutableStateFlow<List<AppNotification>>(initialNotifications)
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    fun addNotification(title: String, message: String, category: NotificationCategory) {
        val notif = AppNotification(title = title, message = message, category = category)
        _notifications.value = listOf(notif) + _notifications.value
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }
}
