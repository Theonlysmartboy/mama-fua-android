package org.tridzen.mamafua.ui.home.launcher.post.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.repository.ProfilesRepository
import org.tridzen.mamafua.data.remote.responses.ProfilesResponse
import javax.inject.Inject

@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val profilesRepository: ProfilesRepository,
) : ViewModel() {

    private var _centerId: MutableLiveData<String> = MutableLiveData()
    val centerId: LiveData<String> = _centerId

    private val _profilesByCenters: MutableLiveData<Resource<ProfilesResponse>> = MutableLiveData()
    val profilesByCenters: LiveData<Resource<ProfilesResponse>> = _profilesByCenters

    fun getCenters() = centerId.observeForever {
        viewModelScope.launch(Dispatchers.Main) {
            val profs = profilesRepository.getProfilesByCenter(it)
            _profilesByCenters.value = profs.value
        }
    }

    fun setCenterId(id: String) {
        _centerId.value = id
    }
}