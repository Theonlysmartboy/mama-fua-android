package org.tridzen.mamafua.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import org.tridzen.mamafua.data.local.entities.Cart

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntry(cart: Cart)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: List<Cart>)

    @Delete
    suspend fun removeEntry(cart: Cart)

    @Query("UPDATE cart SET count=:count WHERE id = :id")
    suspend fun updateEntry(count: Int, id: String)

    @Query("SELECT * FROM cart")
    fun getCart(): LiveData<MutableList<Cart>>

    @Query("SELECT * FROM cart WHERE id = :id LIMIT 1")
    suspend fun getCartItem(id: String): Cart?

    @Query("DELETE FROM cart")
    fun clearCart()
}