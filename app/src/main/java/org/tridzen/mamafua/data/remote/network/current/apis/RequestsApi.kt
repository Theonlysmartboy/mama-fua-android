package org.tridzen.mamafua.data.remote.network.current.apis

import org.json.JSONObject
import org.tridzen.mamafua.data.local.entities.Job
import org.tridzen.mamafua.data.remote.responses.JobResponse
import org.tridzen.mamafua.data.remote.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface RequestsApi {

    @POST("requests/create")
    suspend fun sendRequest(@Body job: Job): JobResponse

    @PUT("/auth/update/{_id}")
    suspend fun updateUser(
        @Query("_id") _id: String,
        @Body update: JSONObject
    ): LoginResponse
}