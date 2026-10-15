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

private val DarkColorScheme = darkColorScheme(
  primary = BookAmber,
  onPrimary = Color.Black,
  primaryContainer = BookNavyDark,
  onPrimaryContainer = BookAmberLight,
  secondary = BookAmberLight,
  onSecondary = Color.Black,
  secondaryContainer = DarkPaperVariant,
  onSecondaryContainer = Color.White,
  tertiary = BookTerracotta,
  onTertiary = Color.White,
  background = DarkPaper,
  onBackground = TextPrimaryDark,
  surface = DarkCardBg,
  onSurface = TextPrimaryDark,
  surfaceVariant = DarkPaperVariant,
  onSurfaceVariant = TextSecondaryDark,
  outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
  primary = BookNavy,
  onPrimary = Color.White,
  primaryContainer = BookAmberContainer,
  onPrimaryContainer = BookAmber,
  secondary = BookAmber,
  onSecondary = Color.White,
  secondaryContainer = WarmPaperVariant,
  onSecondaryContainer = BookNavy,
  tertiary = BookTerracotta,
  onTertiary = Color.White,
  background = WarmPaper,
  onBackground = TextPrimaryLight,
  surface = WarmCardBg,
  onSurface = TextPrimaryLight,
  surfaceVariant = WarmPaperVariant,
  onSurfaceVariant = TextSecondaryLight,
  outline = WarmBorder
)

@Composable
fun BookverseTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep bespoke bookstore branding aesthetic by default
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

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
