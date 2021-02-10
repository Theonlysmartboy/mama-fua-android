package org.tridzen.mamafua.ui.home.launcher.history.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.tridzen.mamafua.data.local.entities.News
import org.tridzen.mamafua.data.remote.network.withdi.Resource
import org.tridzen.mamafua.data.remote.repository.NewsRepository
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    repository: NewsRepository
) : ViewModel() {

    val characters: LiveData<Resource<List<News>>> = repository.getCharacters()
}
