package org.tridzen.mamafua.data.remote.network.current.apis

import org.tridzen.mamafua.data.remote.responses.ReferralResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReferralsApi {

    @GET("referrals/{code}")
    suspend fun getReferral(@Path("code") code: String): ReferralResponse
}