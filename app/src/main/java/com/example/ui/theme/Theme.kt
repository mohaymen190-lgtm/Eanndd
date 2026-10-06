package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme =
    lightColorScheme(
        primary = SelvaPrimaryGreen,
        onPrimary = Color.White,
        primaryContainer = SelvaLightGreen,
        onPrimaryContainer = Color(0xFF094323),
        secondary = SelvaSecondaryGreen,
        onSecondary = Color.White,
        secondaryContainer = SelvaMintSoft,
        onSecondaryContainer = Color(0xFF143B1B),
        background = SelvaLightBg,
        onBackground = SelvaTextPrimary,
        surface = SelvaLightSurface,
        onSurface = SelvaTextPrimary,
        surfaceVariant = SelvaLightSurfaceVariant,
        onSurfaceVariant = SelvaTextSecondary,
        outline = SelvaBorderLight,
        outlineVariant = Color(0xFFE4ECE5)
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFF4ADE80),
        onPrimary = Color(0xFF003919),
        primaryContainer = Color(0xFF0E532C),
        onPrimaryContainer = Color(0xFFB4F2C8),
        secondary = Color(0xFF86EFAC),
        background = Color(0xFF121A15),
        surface = Color(0xFF17221A),
        surfaceVariant = Color(0xFF203025),
        onBackground = Color(0xFFE6EFE8),
        onSurface = Color(0xFFE6EFE8),
        onSurfaceVariant = Color(0xFFB0C4B5),
        outline = Color(0xFF334A3A)
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Default to serene light design as requested by user
    dynamicColor: Boolean = false, // Keep consistent bespoke palette
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
