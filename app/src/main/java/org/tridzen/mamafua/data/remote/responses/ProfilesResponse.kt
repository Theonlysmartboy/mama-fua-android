package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.Profile

data class ProfilesResponse (
    val message: String,
    val profiles: List<Profile>
)