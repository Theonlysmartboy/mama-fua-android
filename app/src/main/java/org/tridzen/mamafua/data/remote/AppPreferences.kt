package org.tridzen.mamafua.data.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "my_data_store"
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
        val ORDER_TO_BE_SENT = stringPreferencesKey("order_to_be_sent")
        val IS_FIRST_TIME_LAUNCH = booleanPreferencesKey("is_first_time_launch")
        val THEME_PREFS = stringPreferencesKey("ThemePrefs")
    }
}