package org.tridzen.mamafua.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tridzen.mamafua.data.local.entities.Profile

@Dao
interface ProfilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllProfiles(quotes: List<Profile>)

    @Query("SELECT * FROM Profile")
    fun getProfiles(): LiveData<List<Profile>>

}