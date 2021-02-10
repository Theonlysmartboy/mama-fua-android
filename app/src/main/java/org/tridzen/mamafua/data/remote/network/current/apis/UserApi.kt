package org.tridzen.mamafua.data.remote.network.current.apis

import okhttp3.ResponseBody
import org.tridzen.mamafua.data.local.entities.Login
import org.tridzen.mamafua.data.local.entities.SignUp
import org.tridzen.mamafua.data.remote.responses.LoginResponse
import retrofit2.http.*

interface UserApi {

    @GET("/auth/user")
    suspend fun getUser(): LoginResponse

    @PUT("/auth/user/{userId}")
    suspend fun <T> updateUser(@Query("userId") userId: String, @Body update: T): LoginResponse

    @POST("logout")
    suspend fun logout(): ResponseBody

    @POST("auth/login")
    suspend fun login(
        @Body user: Login
    ): LoginResponse

    @PUT("auth/signup")
    suspend fun register(
        @Body user: SignUp
    ): LoginResponse
}