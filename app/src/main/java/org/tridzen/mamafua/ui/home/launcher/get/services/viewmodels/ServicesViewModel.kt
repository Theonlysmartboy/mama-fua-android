package org.tridzen.mamafua.ui.home.launcher.get.services.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.tridzen.mamafua.data.remote.repository.ServiceRepository
import org.tridzen.mamafua.utils.base.BaseViewModel
import org.tridzen.mamafua.utils.coroutines.lazyDeferred
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(private val repository: ServiceRepository) :
    BaseViewModel(repository) {

    val services by lazyDeferred {
        repository.getServices()
    }

    private val _washMode: MutableLiveData<String> = MutableLiveData("Manual")
    val washMode: LiveData<String>
        get() = _washMode

    fun switchWashMode() {
        if (_washMode.value!! == "Manual") {
            _washMode.value = "Machine"
            return
        }

        if (_washMode.value!! == "Machine") {
            _washMode.value = "Manual"
            return
        }
    }
}