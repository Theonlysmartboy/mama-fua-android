package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.News

data class NewsResponse(val message: String, val news: List<News>)
