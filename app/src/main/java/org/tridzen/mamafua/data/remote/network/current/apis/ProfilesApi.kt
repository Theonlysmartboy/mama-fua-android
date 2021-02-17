package org.tridzen.mamafua.data.remote.network.current.apis

import org.tridzen.mamafua.data.remote.responses.ProfilesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfilesApi {

    @GET("profiles/findbycenter/{centerId}")
    suspend fun getProfilesByCenter(@Query("centerId") centerId: String): ProfilesResponse
}