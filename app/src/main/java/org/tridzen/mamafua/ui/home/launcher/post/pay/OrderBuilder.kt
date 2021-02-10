package org.tridzen.mamafua.ui.home.launcher.post.pay

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderBuilder(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "order_builder"
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
        val FULFILLER_NAME = stringPreferencesKey("FULFILLER_NAME")
        val FULFILLER_ID = stringPreferencesKey("FULFILLER_ID")
        val DELIVERY_DATE = stringPreferencesKey("DELIVERY_DATE")
        val CENTER = stringPreferencesKey("CENTER")
        val LONGITUDE = doublePreferencesKey("LONGITUDE")
        val LATITUDE = doublePreferencesKey("LATITUDE")
        val PAID_VIA = stringPreferencesKey("PAID_VIA")
        val PHONE = stringPreferencesKey("PHONE")
        val PLACED_BY = stringPreferencesKey("PLACED_BY")
        val AMOUNT = stringPreferencesKey("AMOUNT")
        val TRANSACTION_ID = stringPreferencesKey("TRANSACTION_ID")
    }
}