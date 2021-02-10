package org.tridzen.mamafua.data.remote.network.withdi

import org.tridzen.mamafua.data.remote.responses.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {
    @GET("news")
    suspend fun getAllCharacters() : Response<NewsResponse>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<NewsResponse>
}