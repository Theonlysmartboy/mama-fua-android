package org.tridzen.mamafua.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.local.entities.Login
import org.tridzen.mamafua.data.local.entities.SignUp
import org.tridzen.mamafua.data.local.entities.User
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.network.current.apis.UserApi
import org.tridzen.mamafua.utils.Constants
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.data.Prefs

class UserRepository(
    private val api: UserApi,
    private val db: AppDatabase,
    private val prefs: Prefs
) : BaseRepository() {

    private val _user: MutableLiveData<User> = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        user.observeForever {
            saveUser(it)
        }
    }

    fun saveUser(user: User) = Coroutines.io {
        prefs.setString(Prefs.USER_SAVED_AT, LocalDateTime.now().toString())
        db.getUserDao().upsert(user)
    }

    suspend fun <T> updateUser(
        update: T, userId: String
    ) = safeApiCall {
        api.updateUser(userId, update)
    }

    suspend fun fetchUser(): LiveData<User> {
        val lastSavedAt = prefs.getString(Prefs.USER_SAVED_AT)
        if ((lastSavedAt.isEmpty()) || isFetchNeeded(LocalDateTime.parse(lastSavedAt))) {
            try {
                when (val response = safeApiCall { api.getUser() }) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> _user.postValue(response.value.user)

                    is Resource.Failure -> {
                        Log.d("User", response.isNetworkError.toString())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return user
    }

    suspend fun getUser(): LiveData<User> {
        return withContext(Dispatchers.IO) {
            fetchUser()
            db.getUserDao().getUser()
        }
    }

    suspend fun login(
        user: Login
    ) = safeApiCall {
        api.login(user)
    }

    suspend fun register(
        user: SignUp
    ) = safeApiCall {
        api.register(user)
    }

    fun saveAuthToken(token: String) {
        prefs.setString(Prefs.KEY_AUTH, token)
    }

    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.MINUTES.between(
            savedAt,
            LocalDateTime.now()
        ) > Constants.MINIMUM_INTERVAL_USER
    }
}