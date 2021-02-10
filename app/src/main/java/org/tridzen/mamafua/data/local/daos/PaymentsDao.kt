package org.tridzen.mamafua.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tridzen.mamafua.data.local.entities.Payment

@Dao
interface PaymentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPayment(payment: Payment)

    @Query("DELETE FROM payment where number = :number")
    suspend fun deleteMode(number: String)

    @Query("SELECT * FROM payment")
    fun getModes(): LiveData<List<Payment>>

    @Query("UPDATE payment SET number= :number AND name = :name WHERE id = :id")
    suspend fun editMode(number: String, name: String, id: Int)
}