package org.tridzen.mamafua.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.network.current.apis.ProfilesApi
import org.tridzen.mamafua.data.remote.responses.ProfilesResponse
import org.tridzen.mamafua.utils.Constants
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.tryOrNull

class ProfilesRepository(
    private val api: ProfilesApi,
    private val db: AppDatabase,
    private val prefs: AppPreferences
) : BaseRepository() {

    private val _profiles = MutableLiveData<Resource<ProfilesResponse>>()
    val profiles: LiveData<Resource<ProfilesResponse>> = _profiles

    private suspend fun fetchProfiles(): LiveData<Resource<ProfilesResponse>> {
//        val lastSavedAt = prefs.getValue(AppPreferences.PROFILES_SAVED_AT)
//
//        if (lastSavedAt.first() == null || isFetchNeeded(LocalDateTime.parse(lastSavedAt.first()))) {
        try {
            val response = safeApiCall { api.getProfiles() }
            _profiles.postValue(response)
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        }
        return profiles
    }

    suspend fun getProfilesByCenter(id: String): LiveData<Resource<ProfilesResponse>> {
        tryOrNull {
            val response = safeApiCall { api.getProfilesByCenter(id) }
            _profiles.postValue(response)
        }

        return profiles
    }

    suspend fun getProfiles(): LiveData<Resource<ProfilesResponse>> {
        return withContext(Dispatchers.IO) {
            fetchProfiles()
        }
    }

    private fun saveProfiles(list: List<Profile>) {
        Coroutines.io {
            prefs.saveValue(LocalDateTime.now().toString(), AppPreferences.PROFILES_SAVED_AT)
            db.getProfilesDao().saveAllProfiles(list)
        }
    }

    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.SECONDS.between(
            savedAt,
            LocalDateTime.now()
        ) > Constants.MINIMUM_INTERVAL_PROFILES
    }
}
