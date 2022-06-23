package com.example.jetpackcomposedemo.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ColorPalette = lightColors(
        primary = colorPrimary,
        primaryVariant = colorPrimary,
        secondary = colorPrimary

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun JetPackComposeDemoTheme(content: @Composable() () -> Unit) {

    MaterialTheme(
            colors = ColorPalette,
            typography = typography,
            shapes = shapes,
            content = content
    )
}