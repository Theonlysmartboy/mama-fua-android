package org.tridzen.mamafua.di.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.network.current.RemoteDataSource
import org.tridzen.mamafua.data.remote.network.current.apis.ProfilesApi
import org.tridzen.mamafua.data.remote.repository.ProfilesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfilesModule {

    @Provides
    @Singleton
    fun provideProfilesRepository(
        api: ProfilesApi,
        db: AppDatabase,
        prefs: AppPreferences
    ): ProfilesRepository {
        return ProfilesRepository(api, db, prefs)
    }

    @Singleton
    @Provides
    fun provideProfilesApi(
        remoteDataSource: RemoteDataSource,
    ): ProfilesApi {
        return remoteDataSource.buildApi(ProfilesApi::class.java)
    }
}