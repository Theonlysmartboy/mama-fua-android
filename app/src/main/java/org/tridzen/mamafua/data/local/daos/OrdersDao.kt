package org.tridzen.mamafua.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.tridzen.mamafua.data.local.entities.Order

@Dao
interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<Order>)

    @Query("delete from orders where _id = :id")
    suspend fun delete(id: Int)

    @Query("delete from orders")
    suspend fun clear()

    @Query("SELECT * FROM orders")
    fun getOrders(): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE :id= _id")
    fun getOrder(id: String): LiveData<Order>
}