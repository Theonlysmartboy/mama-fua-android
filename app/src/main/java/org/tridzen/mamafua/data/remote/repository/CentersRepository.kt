package org.tridzen.mamafua.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.network.current.apis.CentersApi
import org.tridzen.mamafua.data.remote.responses.CentersResponse

class CentersRepository(private val api: CentersApi) : BaseRepository() {

    private val _centers = MutableLiveData<Resource<CentersResponse>>()
    private val centers: LiveData<Resource<CentersResponse>> get() = _centers

    suspend fun fetchProfiles(): LiveData<Resource<CentersResponse>> {
        try {
            val response = safeApiCall { api.getCenters() }
            _centers.postValue(response)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return centers
    }
}
