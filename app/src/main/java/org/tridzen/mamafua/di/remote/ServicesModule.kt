package org.tridzen.mamafua.di.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.remote.network.current.RemoteDataSource
import org.tridzen.mamafua.data.remote.network.current.apis.ServicesApi
import org.tridzen.mamafua.data.remote.repository.ServiceRepository
import org.tridzen.mamafua.utils.data.Prefs
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Provides
    @Singleton
    fun provideServicesRepository(
        api: ServicesApi,
        db: AppDatabase,
        prefs: Prefs
    ): ServiceRepository {
        return ServiceRepository(api, db, prefs)
    }

    @Singleton
    @Provides
    fun provideServicesApi(
        remoteDataSource: RemoteDataSource,
    ): ServicesApi {
        return remoteDataSource.buildApi(ServicesApi::class.java)
    }
}