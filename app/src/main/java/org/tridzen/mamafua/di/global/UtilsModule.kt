package org.tridzen.mamafua.di.global

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.ui.home.launcher.post.pay.OrderBuilder
import org.tridzen.mamafua.utils.data.Prefs
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideOrderBuilder(@ApplicationContext context: Context): OrderBuilder {
        return OrderBuilder(context)
    }

    @Singleton
    @Provides
    fun provideDatastorePreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }

    @Singleton
    @Provides
    fun provideNormalPreferences(@ApplicationContext context: Context): Prefs {
        return Prefs(context)
    }
}