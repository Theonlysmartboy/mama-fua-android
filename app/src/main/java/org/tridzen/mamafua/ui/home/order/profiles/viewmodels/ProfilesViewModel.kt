package org.tridzen.mamafua.ui.home.order.profiles.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.repository.ProfilesRepository
import org.tridzen.mamafua.data.remote.responses.ProfilesResponse
import org.tridzen.mamafua.utils.base.BaseViewModel
import org.tridzen.mamafua.utils.reactive.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val profilesRepository: ProfilesRepository,
) : BaseViewModel(profilesRepository) {

    private val _data: SingleLiveEvent<Resource<ProfilesResponse>> = SingleLiveEvent()
    val data: SingleLiveEvent<Resource<ProfilesResponse>> = _data

    fun getProfile(): LiveData<Profile> = profilesRepository.getProfile()

    fun getData(id: String) = viewModelScope.launch {
        _data.value = profilesRepository.getProfilesByCenter(id)
    }

    fun saveProfile(profile: Profile) = viewModelScope.launch {
        profilesRepository.saveProfile(profile)
    }
}