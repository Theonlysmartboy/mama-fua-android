package org.tridzen.mamafua.ui.home.launcher.apply

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.tridzen.mamafua.data.local.entities.Job
import org.tridzen.mamafua.data.remote.repository.JobRepository
import org.tridzen.mamafua.utils.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(private val repository: JobRepository) :
    BaseViewModel(repository) {

    fun sendApplication(job: Job, userId: String) = viewModelScope.launch {
        repository.sendApplication(job)
        val update = JSONObject()
        update.put("requesting", false)
        repository.updateUser(update, userId)
    }
}