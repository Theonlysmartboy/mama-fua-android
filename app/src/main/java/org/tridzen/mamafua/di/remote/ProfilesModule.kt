package org.tridzen.mamafua.di.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.remote.network.current.RemoteDataSource
import org.tridzen.mamafua.data.remote.network.current.apis.CentersApi
import org.tridzen.mamafua.data.remote.network.current.apis.ProfilesApi
import org.tridzen.mamafua.data.remote.repository.CentersRepository
import org.tridzen.mamafua.data.remote.repository.ProfilesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfilesModule {

    @Provides
    @Singleton
    fun provideProfilesRepository(
        api: ProfilesApi,
        db: AppDatabase
    ): ProfilesRepository {
        return ProfilesRepository(api, db)
    }

    @Provides
    @Singleton
    fun provideCentersRepository(api: CentersApi): CentersRepository {
        return CentersRepository(api)
    }

    @Singleton
    @Provides
    fun provideProfilesApi(
        remoteDataSource: RemoteDataSource,
    ): ProfilesApi {
        return remoteDataSource.buildApi(ProfilesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCentersApi(remoteDataSource: RemoteDataSource): CentersApi {
        return remoteDataSource.buildApi(CentersApi::class.java)
    }
}