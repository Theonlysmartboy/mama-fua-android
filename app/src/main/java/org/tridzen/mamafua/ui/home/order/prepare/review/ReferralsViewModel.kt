package org.tridzen.mamafua.ui.home.order.prepare.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.repository.ReferralsRepository
import org.tridzen.mamafua.data.remote.responses.ReferralResponse
import javax.inject.Inject

@HiltViewModel
class ReferralsViewModel @Inject constructor(private val referralsRepository: ReferralsRepository) :
    ViewModel() {

    private var response: MutableLiveData<Resource<ReferralResponse>> = MutableLiveData()

    fun getReferral(code: String): LiveData<Resource<ReferralResponse>> {
        viewModelScope.launch {
            response.value = referralsRepository.getReferral(code)
        }
        return response
    }
}