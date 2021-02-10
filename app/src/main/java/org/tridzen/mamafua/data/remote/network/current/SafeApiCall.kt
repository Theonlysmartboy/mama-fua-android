package org.tridzen.mamafua.data.remote.network.current

import org.json.JSONException
import org.json.JSONObject
import org.tridzen.mamafua.utils.data.ApiException

import retrofit2.Response

abstract class SafeApiCall {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
            }

            throw ApiException(message.toString())
        }
    }
}