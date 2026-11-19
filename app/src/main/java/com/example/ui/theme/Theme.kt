package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NoorChampagneGold,
    onPrimary = NoorMidnightNavy,
    primaryContainer = NoorDeepRoyalBlue,
    onPrimaryContainer = NoorSoftGold,
    secondary = NoorSoftGold,
    onSecondary = NoorMidnightNavy,
    secondaryContainer = NoorNavySurfaceElevated,
    onSecondaryContainer = NoorIvory,
    tertiary = NoorDeepRoyalBlue,
    onTertiary = NoorIvory,
    background = NoorMidnightNavy,
    onBackground = NoorIvory,
    surface = NoorNavySurface,
    onSurface = NoorIvory,
    surfaceVariant = NoorNavySurfaceElevated,
    onSurfaceVariant = NoorSecondaryText,
    outline = NoorDivider
)

private val LightColorScheme = lightColorScheme(
    primary = NoorLightPrimary,
    onPrimary = NoorLightOnPrimary,
    primaryContainer = NoorLightCard,
    onPrimaryContainer = NoorLightPrimary,
    secondary = NoorLightGold,
    onSecondary = NoorLightBackground,
    secondaryContainer = NoorLightCard,
    onSecondaryContainer = NoorLightTextPrimary,
    tertiary = NoorLightGold,
    onTertiary = NoorLightBackground,
    background = NoorLightBackground,
    onBackground = NoorLightTextPrimary,
    surface = NoorLightSurface,
    onSurface = NoorLightTextPrimary,
    surfaceVariant = NoorLightCard,
    onSurfaceVariant = NoorLightTextSecondary,
    outline = NoorLightDivider
)

@Composable
fun NoorTheme(
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
