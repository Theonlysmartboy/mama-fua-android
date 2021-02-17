package org.tridzen.mamafua.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.tridzen.mamafua.data.local.entities.Login
import org.tridzen.mamafua.data.local.entities.SignUp
import org.tridzen.mamafua.data.local.entities.User
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.repository.UserRepository
import org.tridzen.mamafua.data.remote.responses.LoginResponse
import org.tridzen.mamafua.utils.base.BaseViewModel
import org.tridzen.mamafua.utils.coroutines.lazyDeferred
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    val getLoggedInUser by lazyDeferred {
        repository.getUser()
    }

    val user = repository.user

    val freshUser by lazyDeferred {
        repository.fetchUser()
    }

    fun saveUser(user: User) = viewModelScope.launch {
        repository.saveUser(user)
    }

    fun <T> updateUser(userId: String, update: T) = viewModelScope.launch {
        repository.updateUser(update, userId)
    }

    fun login(
        user: Login
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(user)
    }

    fun register(
        user: SignUp
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.register(user)
    }

    fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }
}