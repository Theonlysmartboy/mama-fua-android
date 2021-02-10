package org.tridzen.mamafua

import android.app.Application
import androidx.lifecycle.asLiveData
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.THEME_PREFS
import org.tridzen.mamafua.utils.ThemeHelper
import timber.log.Timber

@HiltAndroidApp
@Module
@InstallIn(SingletonComponent::class)
class FuaApplication() : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        val themePrefs = AppPreferences(this).getValue(THEME_PREFS)
        themePrefs.asLiveData().observeForever {
            if (it != null) {
                ThemeHelper.applyTheme(it)
            }
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}