package com.nutriscan.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Green400,
    onPrimary = Green950,
    primaryContainer = Green800,
    onPrimaryContainer = Green100,
    secondary = Green300,
    onSecondary = Green900,
    secondaryContainer = Green700,
    onSecondaryContainer = Green100,
    tertiary = Green500,
    onTertiary = Cream,
    tertiaryContainer = Green800,
    onTertiaryContainer = Green100,
    background = SurfaceDark,
    onBackground = Green50,
    surface = SurfaceDark,
    onSurface = Green50,
    surfaceVariant = SurfaceDarkVariant,
    onSurfaceVariant = Green200,
    surfaceTint = Green400,
    inverseSurface = Green50,
    inverseOnSurface = Green900,
    outline = Green600,
    outlineVariant = Green700,
    error = Color(0xFFBA1A1A),
    onError = Cream,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

private val LightColorScheme = lightColorScheme(
    primary = Green700,
    onPrimary = Cream,
    primaryContainer = Green100,
    onPrimaryContainer = Green900,
    secondary = Green600,
    onSecondary = Cream,
    secondaryContainer = Green100,
    onSecondaryContainer = Green900,
    tertiary = Green700,
    onTertiary = Cream,
    tertiaryContainer = Green100,
    onTertiaryContainer = Green900,
    background = Cream,
    onBackground = Green900,
    surface = Cream,
    onSurface = Green900,
    surfaceVariant = Green50,
    onSurfaceVariant = Green700,
    surfaceTint = Green700,
    inverseSurface = Green900,
    inverseOnSurface = Green50,
    outline = Green400,
    outlineVariant = Green200,
    error = Color(0xFFBA1A1A),
    onError = Cream,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

@Composable
fun NutriScanTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
