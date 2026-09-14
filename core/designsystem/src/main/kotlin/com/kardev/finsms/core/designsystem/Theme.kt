package com.kardev.finsms.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FinSmsGreen = Color(0xFF2E7D32)
private val FinSmsRed = Color(0xFFC62828)
private val FinSmsPrimary = Color(0xFF1565C0)

private val LightColors = lightColorScheme(
    primary = FinSmsPrimary,
    secondary = FinSmsGreen,
    error = FinSmsRed
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),
    secondary = Color(0xFF81C784),
    error = Color(0xFFEF9A9A)
)

object FinSmsColors {
    val Debit = FinSmsRed
    val Credit = FinSmsGreen
}

@Composable
fun FinSmsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
