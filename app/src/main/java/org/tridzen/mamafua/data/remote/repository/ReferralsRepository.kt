package org.tridzen.mamafua.data.remote.repository

import org.tridzen.mamafua.data.remote.network.current.apis.ReferralsApi
import javax.inject.Inject

class ReferralsRepository @Inject constructor(
    private val api: ReferralsApi
) : BaseRepository() {

    suspend fun getReferral(code: String) = safeApiCall { api.getReferral(code) }
}