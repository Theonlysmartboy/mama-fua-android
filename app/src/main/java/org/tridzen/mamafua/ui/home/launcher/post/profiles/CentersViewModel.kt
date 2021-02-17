package org.tridzen.mamafua.ui.home.launcher.post.profiles

import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.tridzen.mamafua.data.remote.repository.CentersRepository
import org.tridzen.mamafua.utils.base.BaseViewModel
import org.tridzen.mamafua.utils.coroutines.lazyDeferred
import javax.inject.Inject

@HiltViewModel
class CentersViewModel @Inject constructor(private val centersRepository: CentersRepository): BaseViewModel(centersRepository) {

    val centers by lazyDeferred {
        liveData {
            emit(centersRepository.fetchProfiles())
        }
    }
}