package com.example.soniccanvas.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

object VectorArtGenerator {
    fun generateAndSaveBitmap(context: Context, seed: Long = System.currentTimeMillis(), size: Int = 512): String? {
        try {
            val random = Random(seed)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val paint = Paint().apply {
                isAntiAlias = true
            }

            // Base background color
            val r1 = random.nextInt(20, 150)
            val g1 = random.nextInt(20, 150)
            val b1 = random.nextInt(20, 150)
            val baseColor = android.graphics.Color.rgb(r1, g1, b1)
            canvas.drawColor(baseColor)

            // Draw random abstract geometric circles & arcs
            repeat(random.nextInt(4, 9)) {
                val r = random.nextInt(50, 255)
                val g = random.nextInt(50, 255)
                val b = random.nextInt(50, 255)
                val a = random.nextInt(100, 220)
                paint.color = android.graphics.Color.argb(a, r, g, b)

                val cx = random.nextFloat() * size
                val cy = random.nextFloat() * size
                val radius = random.nextFloat() * (size / 1.5f)
                canvas.drawCircle(cx, cy, radius, paint)
            }

            val file = File(context.filesDir, "vector_art_$seed.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            return "file://${file.absolutePath}"
        } catch (_: Exception) {
            return null
        }
    }
}
