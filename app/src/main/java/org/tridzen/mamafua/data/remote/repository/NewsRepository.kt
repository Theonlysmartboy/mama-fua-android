package org.tridzen.mamafua.data.remote.repository

import org.tridzen.mamafua.data.local.daos.NewsDao
import org.tridzen.mamafua.data.remote.network.withdi.CharacterRemoteDataSource
import org.tridzen.mamafua.utils.performGetOperation
import javax.inject.Inject

//class NewsRepository(private val api: DataApi): BaseRepository() {
//
//    suspend fun getNews() = safeApiCall {
//        api.getNews()
//    }
//}

class NewsRepository @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: NewsDao
) {

//    fun getCharacter(id: Int) = performGetOperation(
//        databaseQuery = { localDataSource.getCharacter(id) },
//        networkCall = { remoteDataSource.getCharacter(id) },
//        saveCallResult = { localDataSource.insert(it) }
//    )

    fun getCharacters() = performGetOperation(
        databaseQuery = { localDataSource.getAllCharacters() },
        networkCall = { remoteDataSource.getCharacters() },
        saveCallResult = { localDataSource.insertAll(it.news) }
    )
}