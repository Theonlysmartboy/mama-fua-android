package org.tridzen.mamafua.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tridzen.mamafua.data.local.entities.News

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getAllCharacters() : LiveData<List<News>>

    @Query("SELECT * FROM news WHERE _id = :id")
    fun getCharacter(id: Int): LiveData<News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<News>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: News)
}
