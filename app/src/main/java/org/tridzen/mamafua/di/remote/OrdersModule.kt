package org.tridzen.mamafua.di.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.network.current.RemoteDataSource
import org.tridzen.mamafua.data.remote.network.current.apis.OrdersApi
import org.tridzen.mamafua.data.remote.network.current.apis.PurchaseApi
import org.tridzen.mamafua.data.remote.network.current.apis.ReferralsApi
import org.tridzen.mamafua.data.remote.repository.CartRepository
import org.tridzen.mamafua.data.remote.repository.OrdersRepository
import org.tridzen.mamafua.data.remote.repository.PaymentsRepository
import org.tridzen.mamafua.data.remote.repository.ReferralsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrdersModule {

    @Provides
    fun provideOrdersRepository(
        ordersApi: OrdersApi,
        appDatabase: AppDatabase,
        preferences: AppPreferences
    ): OrdersRepository {
        return OrdersRepository(ordersApi, appDatabase, preferences)
    }

    @Provides
    fun provideCartRepository(
        appDatabase: AppDatabase,
    ): CartRepository {
        return CartRepository(appDatabase)
    }

    @Provides
    fun providePaymentsRepository(
        api: PurchaseApi,
        appDatabase: AppDatabase,
    ): PaymentsRepository {
        return PaymentsRepository(appDatabase, api)
    }

    @Provides
    fun provideReferralsRepository(
        api: ReferralsApi,
        appDatabase: AppDatabase,
    ): ReferralsRepository {
        return ReferralsRepository(api)
    }

    @Singleton
    @Provides
    fun provideOrdersApi(
        remoteDataSource: RemoteDataSource,
    ): OrdersApi {
        return remoteDataSource.buildApi(OrdersApi::class.java)
    }

    @Singleton
    @Provides
    fun providePurchaseApi(
        remoteDataSource: RemoteDataSource,
    ): PurchaseApi {
        return remoteDataSource.buildApi(PurchaseApi::class.java)
    }

    @Singleton
    @Provides
    fun provideReferralsApi(
        remoteDataSource: RemoteDataSource,
    ): ReferralsApi {
        return remoteDataSource.buildApi(ReferralsApi::class.java)
    }
}