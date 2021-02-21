package org.tridzen.mamafua.data.remote.repository

import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.network.current.apis.ProfilesApi

class ProfilesRepository(
    private val api: ProfilesApi,
    private val db: AppDatabase
) : BaseRepository() {

    suspend fun getProfilesByCenter(id: String) = safeApiCall { api.getProfilesByCenter(id) }

    suspend fun saveProfile(profile: Profile) {
        db.getProfilesDao().saveProfile(profile)
    }

    fun getProfile() = db.getProfilesDao().getProfile()
}
