package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.Center

data class CentersResponse(val message: String, val centers: List<Center>)