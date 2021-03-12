package org.tridzen.mamafua.data.remote.network.current.apis

import org.tridzen.mamafua.data.local.entities.Job
import org.tridzen.mamafua.data.remote.responses.JobResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestsApi {

    @POST("requests/create")
    suspend fun sendRequest(@Body job: Job): JobResponse
}