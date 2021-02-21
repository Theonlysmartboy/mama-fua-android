package org.tridzen.mamafua.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.tridzen.mamafua.data.local.daos.*
import org.tridzen.mamafua.data.local.entities.*
import org.tridzen.mamafua.utils.data.Converters

@Database(
    entities = [Profile::class, Cart::class, Service::class, News::class, Order::class, User::class, Payment::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProfilesDao(): ProfilesDao
    abstract fun getCartDao(): CartDao
    abstract fun getServicesDao(): ServicesDao
    abstract fun getNewsDao(): NewsDao
    abstract fun getOrdersDao(): OrdersDao
    abstract fun getUserDao(): UserDao
    abstract fun getPaymentsDao(): PaymentsDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MamaFua.db"
            ).fallbackToDestructiveMigration().build()
    }
}