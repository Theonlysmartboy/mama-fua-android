package org.tridzen.mamafua.data.remote.repository

import org.tridzen.mamafua.data.local.entities.Job
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.network.current.apis.RequestsApi

class JobRepository(private val prefs: AppPreferences, private val api: RequestsApi) : BaseRepository() {

    suspend fun sendApplication(job: Job) = safeApiCall {
        api.sendRequest(job)
    }
}
