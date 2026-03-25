package com.drish.moneytracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Coffee,
    onPrimary = Cream,
    primaryContainer = WarmBrownLight,
    onPrimaryContainer = CoffeeDark,
    secondary = CoffeeMedium,
    onSecondary = CreamLight,
    secondaryContainer = WarmBrown,
    onSecondaryContainer = CoffeeDark,
    tertiary = WarmBrownDark,
    onTertiary = CreamLight,
    background = CreamLight,
    onBackground = CoffeeDark,
    surface = Cream,
    onSurface = CoffeeDark,
    surfaceVariant = WarmBrownLight,
    onSurfaceVariant = CoffeeMedium,
    error = NegativeRed
)

private val DarkColorScheme = darkColorScheme(
    primary = WarmBrown,
    onPrimary = CoffeeDark,
    primaryContainer = CoffeeDark,
    onPrimaryContainer = WarmBrownLight,
    secondary = WarmBrownLight,
    onSecondary = CoffeeDark,
    secondaryContainer = CoffeeMedium,
    onSecondaryContainer = CreamLight,
    tertiary = CoffeeMedium,
    onTertiary = CreamLight,
    background = OnSurfaceDark,
    onBackground = CreamLight,
    surface = SurfaceDark,
    onSurface = CreamLight,
    surfaceVariant = CoffeeDark,
    onSurfaceVariant = WarmBrownLight,
    error = NegativeRed
)

@Composable
fun MoneyTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
