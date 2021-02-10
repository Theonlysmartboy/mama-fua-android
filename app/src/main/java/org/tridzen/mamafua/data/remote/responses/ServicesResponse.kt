package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.Service

data class ServicesResponse(val message: String, val services: List<Service>)
