package org.tridzen.mamafua.di.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.network.current.RemoteDataSource
import org.tridzen.mamafua.data.remote.network.current.apis.RequestsApi
import org.tridzen.mamafua.data.remote.network.current.apis.UserApi
import org.tridzen.mamafua.data.remote.repository.JobRepository
import org.tridzen.mamafua.data.remote.repository.UserRepository
import org.tridzen.mamafua.utils.data.Prefs
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }

    @Singleton
    @Provides
    fun provideUserApi(
        remoteDataSource: RemoteDataSource,
    ): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRequestsApi(
        remoteDataSource: RemoteDataSource,
    ): RequestsApi {
        return remoteDataSource.buildApi(RequestsApi::class.java)
    }

    @Provides
    fun provideUserRepository(
        authApi: UserApi,
        appDatabase: AppDatabase,
        userPreferences: Prefs
    ): UserRepository {
        return UserRepository(authApi, appDatabase, userPreferences)
    }

    @Provides
    fun provideJobRepository(
        requestsApi: RequestsApi,
        preferences: AppPreferences
    ): JobRepository {
        return JobRepository(preferences, requestsApi)
    }
}