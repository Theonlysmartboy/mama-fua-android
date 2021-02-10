package org.tridzen.mamafua.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object ThemeHelper {

    const val LIGHT_MODE = "light"
    const val DARK_MODE = "dark"
    const val DEFAULT_MODE = "default"

    fun applyTheme(theme: String) {
        val mode = when (theme) {
            LIGHT_MODE -> AppCompatDelegate.MODE_NIGHT_NO
            DARK_MODE -> AppCompatDelegate.MODE_NIGHT_YES
            else -> {
                when {
                    isAtLeastP() -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    isAtLeastL() -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    else -> AppCompatDelegate.MODE_NIGHT_NO
                }
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun getTheme(context: Context): Flow<String> = flow {
        emit(
            when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_NO -> "Light" // Night mode is not active, we're using the light theme
                else -> "Dark"
                // Night mode is active, we're using dark theme
            }
        )
    }

    private fun isAtLeastP() = Build.VERSION.SDK_INT >= 28
    private fun isAtLeastL() = Build.VERSION.SDK_INT >= 21
}