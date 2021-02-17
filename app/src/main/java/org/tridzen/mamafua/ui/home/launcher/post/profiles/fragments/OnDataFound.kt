package org.tridzen.mamafua.ui.home.launcher.post.profiles.fragments

import androidx.lifecycle.LiveData
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.data.remote.responses.ProfilesResponse

interface OnDataFound {
    fun dataFound(resource: LiveData<Resource<ProfilesResponse>>)
}