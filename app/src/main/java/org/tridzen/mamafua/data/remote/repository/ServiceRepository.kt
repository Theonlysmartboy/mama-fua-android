package org.tridzen.mamafua.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.local.entities.Service
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.network.current.apis.ServicesApi
import org.tridzen.mamafua.data.remote.responses.ServicesResponse
import org.tridzen.mamafua.utils.Constants.Companion.MINIMUM_INTERVAL_CATALOG
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.data.Prefs

class ServiceRepository(
    private val api: ServicesApi,
    private val db: AppDatabase,
    private val prefs: Prefs
) : BaseRepository() {

    private val services = MutableLiveData<Resource<ServicesResponse>>()

    var whichTab: String? = "Itemised"

    init {
        services.observeForever {
            when(it) {
                is Resource.Success -> saveServices(it.value.services)
                is Resource.Failure -> {}
                Resource.Loading -> {}
            }
        }
    }

    private suspend fun fetchServices() {
        val lastSavedAt = getVariable(whichTab)

        if ((lastSavedAt.isEmpty()) || isFetchNeeded(LocalDateTime.parse(lastSavedAt))) {
            try {
                val response = safeApiCall { api.getServices() }
                services.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getServices(): LiveData<List<Service>> {
        return withContext(Dispatchers.IO) {
            fetchServices()
            db.getServicesDao().getServices()
        }
    }

    private fun saveServices(list: List<Service>) {
        Coroutines.io {
            getFunction(whichTab, LocalDateTime.now().toString())
            db.getServicesDao().saveAllServices(list)
        }
    }

    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.SECONDS.between(savedAt, LocalDateTime.now()) > MINIMUM_INTERVAL_CATALOG
    }

    private fun getVariable(whichTab: String?): String {
        return when (whichTab) {
            "Itemised" -> prefs.getString(Prefs.ITEMISED_SAVED_AT)
            "Package" -> prefs.getString(Prefs.PACKAGE_SAVED_AT)
            else -> prefs.getString(Prefs.DELIVERY_SAVED_AT)
        }
    }

    private fun getFunction(whichTab: String?, date: String) {
        when (whichTab) {
            "Itemised" -> prefs.setString(Prefs.ITEMISED_SAVED_AT, date)
            "Package" -> prefs.setString(Prefs.PACKAGE_SAVED_AT, date)
            else -> prefs.setString(Prefs.DELIVERY_SAVED_AT, date)
        }
    }
}