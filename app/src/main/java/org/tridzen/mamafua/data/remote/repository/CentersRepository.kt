package org.tridzen.mamafua.data.remote.repository

import org.tridzen.mamafua.data.remote.network.current.apis.CentersApi

class CentersRepository(private val api: CentersApi) : BaseRepository() {

    suspend fun fetchProfiles() = safeApiCall { api.getCenters() }
}
