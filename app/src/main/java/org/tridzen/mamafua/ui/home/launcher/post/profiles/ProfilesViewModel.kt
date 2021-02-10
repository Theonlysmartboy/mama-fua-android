package org.tridzen.mamafua.ui.home.launcher.post.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.tridzen.mamafua.data.remote.repository.ProfilesRepository
import org.tridzen.mamafua.utils.coroutines.lazyDeferred
import javax.inject.Inject

@HiltViewModel
class ProfilesViewModel@Inject constructor(
    private val profilesRepository: ProfilesRepository,
) : ViewModel() {

    private var _centerId: MutableLiveData<String> = MutableLiveData()
    val centerId: LiveData<String> get() = _centerId

    init {
        centerId.observeForever {
            viewModelScope.launch {
                profilesRepository.getProfilesByCenter(it)
            }
        }
    }

    val profiles by lazyDeferred {
        profilesRepository.getProfiles()
    }

    val profilesByCenter by lazyDeferred {
        centerId.value?.let { profilesRepository.getProfilesByCenter(it) }
    }

    fun setCenterId(id: String) = _centerId.postValue(id)
}