package org.tridzen.mamafua.data.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "DataStore"
    )

    suspend fun <T> saveValue(value: T, key: Preferences.Key<T>) = dataStore.edit { preferences ->
        preferences[key] = value
    }

    fun <T> getValue(key: Preferences.Key<T>): Flow<T?> = dataStore.data.map { preferences ->
        preferences[key]
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        val KEY_AUTH = stringPreferencesKey("key_auth")
        val ITEMISED_SAVED_AT = stringPreferencesKey("itemised_saved_at")
        val PACKAGE_SAVED_AT = stringPreferencesKey("package_saved_at")
        val DELIVERY_SAVED_AT = stringPreferencesKey("delivery_saved_at")
        val ORDERS_SAVED_AT = stringPreferencesKey("orders_saved_at")
        val PROFILES_SAVED_AT = stringPreferencesKey("profiles_saved_at")
        val USER_SAVED_AT = stringPreferencesKey("user_saved_at")
        val REQUEST_SENT = booleanPreferencesKey("request_sent")
        val SHOW_ONBOARDING = booleanPreferencesKey("show_onboarding")
        val SHOW_REFERRALS = booleanPreferencesKey("show_referral")
        val THEME_PREFS = stringPreferencesKey("ThemePrefs")
        val TIME_PREFS = stringPreferencesKey("TimePrefs")
        val LATITUDE_PREFS = doublePreferencesKey("LatitudePrefs")
        val LONGITUDE_PREFS = doublePreferencesKey("LongitudePrefs")
    }
}