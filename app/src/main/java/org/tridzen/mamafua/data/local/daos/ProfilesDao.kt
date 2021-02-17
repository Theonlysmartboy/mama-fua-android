package org.tridzen.mamafua.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.tridzen.mamafua.data.local.entities.Profile

@Dao
interface ProfilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: Profile)

}