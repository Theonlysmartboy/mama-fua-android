package org.tridzen.mamafua.data.remote.network.current.apis

import org.tridzen.mamafua.data.remote.responses.ServicesResponse
import retrofit2.http.GET

interface ServicesApi {

    @GET("services")
    suspend fun getServices(): ServicesResponse
}