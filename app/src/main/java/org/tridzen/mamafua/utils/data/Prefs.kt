package org.tridzen.mamafua.utils.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Prefs(context: Context) {

    companion object {
        const val PREFS_FILENAME = "shared_prefs_name"

        const val KEY_MY_STRING = "my_string"
        const val KEY_MY_BOOLEAN = "my_boolean"
        const val KEY_MY_ARRAY = "string_array"
        const val KEY_MY_OBJECT = "my_object"
        const val KEY_ORDER = "order_to_be-sent"

        const val KEY_AUTH = "key_auth"
        const val ITEMISED_SAVED_AT = "itemised_saved_at"
        const val PACKAGE_SAVED_AT = "package_saved_at"
        const val DELIVERY_SAVED_AT = "delivery_saved_at"
        const val ORDERS_SAVED_AT = "orders_saved_at"
        const val PROFILES_SAVED_AT = "profiles_saved_at"
        const val USER_SAVED_AT = "user_saved_at"
        const val REQUEST_SENT = "request_sent"
        const val ORDER_TO_BE_SENT = "order_to_be_sent"
        const val IS_FIRST_TIME_LAUNCH = "is_first_time_launch"
        const val THEME_PREFS = "ThemePrefs"

        const val CENTER = "ThemePrefs"
        const val DELIVERY_DATE = "ThemePrefs"
        const val FULFILLER_ID = "ThemePrefs"
        const val FULFILLER_NAME = "ThemePrefs"
        const val LONGITUDE = "ThemePrefs"
        const val LATITUDE = "ThemePrefs"
        const val PAID_VIA = "ThemePrefs"
        const val PHONE = "ThemePrefs"
        const val TRANSACTION_ID = "transaction_id"
    }

    private val gson = Gson()
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    fun getString(key: String): String = sharedPrefs.getString(key, "") ?: ""

    fun setString(key: String, value: String) = sharedPrefs.edit { putString(key, value) }

    fun getBoolean(key: String): Boolean = sharedPrefs.getBoolean(key, false)

    fun setBoolean(key: String, value: Boolean) = sharedPrefs.edit { putBoolean(key, value) }

//    var myBoolean: Boolean
//        get() = sharedPrefs.getBoolean(KEY_MY_BOOLEAN, false)
//        set(value) = sharedPrefs.edit { putBoolean(KEY_MY_BOOLEAN, value) }

    //    var myString: String
//        get() = sharedPrefs.getString(KEY_MY_STRING, "") ?: ""
//        set(value) = sharedPrefs.edit { putString(KEY_MY_STRING, value) }

//    var myStringArray: Array<String>
//        get() = sharedPrefs.getStringSet(KEY_MY_ARRAY, emptySet())?.toTypedArray() ?: emptyArray()
//        set(value) = sharedPrefs.edit { putStringSet(KEY_MY_ARRAY, value.toSet()) }

    var myObject: MyObject?
        get() {
            val jsonString = sharedPrefs.getString(KEY_ORDER, null) ?: return null
            return gson.fromJson(jsonString, object : TypeToken<MyObject>() {}.type)
        }
        set(value) = sharedPrefs.edit { putString(KEY_ORDER, gson.toJson(value)) }
}

data class MyObject(val name: String)