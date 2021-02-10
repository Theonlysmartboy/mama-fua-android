package org.tridzen.mamafua.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tridzen.mamafua.data.local.entities.Service

@Dao
interface ServicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllServices(catalog: List<Service>)

    @Query("SELECT * FROM Service")
    fun getServices(): LiveData<List<Service>>
}