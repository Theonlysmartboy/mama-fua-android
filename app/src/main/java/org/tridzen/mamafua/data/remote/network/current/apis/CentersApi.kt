package org.tridzen.mamafua.data.remote.network.current.apis

import org.tridzen.mamafua.data.remote.responses.CentersResponse
import retrofit2.http.GET

interface CentersApi {

    @GET("centers")
    suspend fun getCenters(): CentersResponse

}
