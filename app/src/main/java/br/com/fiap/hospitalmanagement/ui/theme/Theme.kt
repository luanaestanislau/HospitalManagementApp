package br.com.fiap.hospitalmanagement.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val MediStockColorScheme = darkColorScheme(
    primary = MediPrimary,
    onPrimary = MediOnPrimary,
    secondary = MediSecondary,
    onSecondary = MediOnSecondary,
    tertiary = MediTertiary,
    onTertiary = MediOnTertiary,
    background = MediBackground,
    onBackground = MediOnBackground,
    surface = MediSurface,
    onSurface = MediOnSurface,
    error = MediError,
    onError = MediOnPrimary
)

@Composable
fun HospitalManagementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val DarkColorScheme = null
    val LightColorScheme = null
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = MediStockColorScheme,
        typography = Typography,
        content = content
    )
}