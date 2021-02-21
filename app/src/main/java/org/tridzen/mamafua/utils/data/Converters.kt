package org.tridzen.mamafua.utils.data

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.tridzen.mamafua.data.local.entities.*
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartFragment

class Converters {

    companion object {
        @TypeConverter
        @JvmStatic
        fun makeListFromString(value: String): List<String> {
            val listType = object : TypeToken<List<String>>() {

            }.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun makeStringFromList(list: List<String>): String {
            val gson = Gson()
            return gson.toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun makeServiceFromString(value: String): Service {
            val profileType = object : TypeToken<Service>() {

            }.type
            return Gson().fromJson(value, profileType)
        }

        @TypeConverter
        @JvmStatic
        fun makeStringFromService(service: Service): String {
            val gson = Gson()
            return gson.toJson(service)
        }

        @TypeConverter
        @JvmStatic
        fun makeCartXFromString(value: String): List<CartX> {
            val profileType = object : TypeToken<List<CartX>>() {

            }.type
            return Gson().fromJson(value, profileType)
        }

        @TypeConverter
        @JvmStatic
        fun makeStringFromCartX(cartX: List<CartX>): String {
            val gson = Gson()
            return gson.toJson(cartX)
        }

        @TypeConverter
        @JvmStatic
        fun makeUserFromString(value: String): User {
            val profileType = object : TypeToken<User>() {

            }.type
            return Gson().fromJson(value, profileType)
        }

        @TypeConverter
        @JvmStatic
        fun makeStringFromLatLng(user: CartFragment.LatLng): String {
            val gson = Gson()
            return gson.toJson(user)
        }

        @TypeConverter
        @JvmStatic
        fun makeLatLngFromString(value: String): CartFragment.LatLng {
            val profileType = object : TypeToken<LatLng>() {

            }.type
            return Gson().fromJson(value, profileType)
        }

        @TypeConverter
        @JvmStatic
        fun makeStringFromUser(user: User): String {
            val gson = Gson()
            return gson.toJson(user)
        }

        @TypeConverter
        @JvmStatic
        fun makeStringFromListOfCart(list: List<Cart>): String {
            val gson = Gson()
            return gson.toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun makeListOfCartFromString(value: String): List<Cart> {
            val listType = object : TypeToken<List<Cart>>() {

            }.type
            return Gson().fromJson(value, listType)
        }


        @TypeConverter
        @JvmStatic
        fun makeStringFromListOfOrders(list: List<Order>): String {
            val gson = Gson()
            return gson.toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun makeListOfOrdersFromString(value: String): List<Order> {
            val listType = object : TypeToken<List<Order>>() {
            }.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun makeStringFromListOfNews(list: List<News>): String {
            val gson = Gson()
            return gson.toJson(list)
        }

        @TypeConverter
        @JvmStatic
        fun makeListOfNewsFromString(value: String): List<News> {
            val listType = object : TypeToken<List<News>>() {

            }.type
            return Gson().fromJson(value, listType)
        }
    }
}