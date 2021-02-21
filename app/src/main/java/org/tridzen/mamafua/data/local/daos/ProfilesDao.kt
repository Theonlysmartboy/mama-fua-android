package org.tridzen.mamafua.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tridzen.mamafua.data.local.entities.CURRENT_PROFILE_ID
import org.tridzen.mamafua.data.local.entities.Profile

@Dao
interface ProfilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: Profile)

    @Query("SELECT * FROM profile WHERE uid = $CURRENT_PROFILE_ID")
    fun getProfile(): LiveData<Profile>

}