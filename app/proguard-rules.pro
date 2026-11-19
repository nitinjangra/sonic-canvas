# Room Database
-keep class com.pixelpushers.sonixa.data.local.entity.** { *; }
-keep class com.pixelpushers.sonixa.data.local.dao.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }

# Dagger Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Media3 ExoPlayer
-keep class androidx.media3.** { *; }

# Moshi / Serialization
-keep class com.pixelpushers.sonixa.data.** { *; }
-keepattributes Signature, InnerClasses, EnclosingMethod

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler
