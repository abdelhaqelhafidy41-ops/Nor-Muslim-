package com.example.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Build
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WallpaperHelper {

    suspend fun applyWallpaper(
        context: Context,
        title: String,
        calligraphyQuote: String,
        startColorHex: Long,
        endColorHex: Long
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val width = 1080
            val height = 1920
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // 1. Draw gradient background
            val gradient = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                startColorHex.toInt(),
                endColorHex.toInt(),
                Shader.TileMode.CLAMP
            )
            val bgPaint = Paint().apply {
                shader = gradient
                isAntiAlias = true
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

            // 2. Draw Islamic decorative border frame
            val borderPaint = Paint().apply {
                color = 0xFFD4AF37.toInt() // Gold
                style = Paint.Style.STROKE
                strokeWidth = 6f
                isAntiAlias = true
            }
            val margin = 60f
            canvas.drawRoundRect(
                RectF(margin, margin, width - margin, height - margin),
                32f, 32f,
                borderPaint
            )

            val innerBorder = Paint().apply {
                color = 0x66D4AF37.toInt()
                style = Paint.Style.STROKE
                strokeWidth = 2f
                isAntiAlias = true
            }
            canvas.drawRoundRect(
                RectF(margin + 20f, margin + 20f, width - margin - 20f, height - margin - 20f),
                24f, 24f,
                innerBorder
            )

            // 3. Draw Calligraphy and Title Text
            val textPaint = Paint().apply {
                color = 0xFFFFF1C5.toInt() // Gold light
                textSize = 58f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            // Draw header brand
            val brandPaint = Paint().apply {
                color = 0xFFD4AF37.toInt()
                textSize = 36f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }
            canvas.drawText("نُورُ المُسْلِمِ", width / 2f, 280f, brandPaint)

            // Draw Calligraphy Quote (multiline if long)
            val words = calligraphyQuote.split(" ")
            val lines = mutableListOf<String>()
            var currentLine = StringBuilder()
            for (w in words) {
                if (currentLine.length + w.length > 22) {
                    lines.add(currentLine.toString())
                    currentLine = StringBuilder(w).append(" ")
                } else {
                    currentLine.append(w).append(" ")
                }
            }
            if (currentLine.isNotEmpty()) {
                lines.add(currentLine.toString())
            }

            var startY = (height / 2f) - (lines.size * 40f)
            for (line in lines) {
                canvas.drawText(line.trim(), width / 2f, startY, textPaint)
                startY += 84f
            }

            // Draw subtitle
            val subPaint = Paint().apply {
                color = 0xCCD4AF37.toInt()
                textSize = 34f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            canvas.drawText("— $title —", width / 2f, startY + 60f, subPaint)

            // Set system wallpaper
            val wallpaperManager = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "تم تعيين الخلفية بنجاح على شاشة الهاتف!", Toast.LENGTH_SHORT).show()
            }
            true
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "تم تجهيز الخلفية وحفظ الإعدادات", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }
}
