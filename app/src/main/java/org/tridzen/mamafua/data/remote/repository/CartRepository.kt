package org.tridzen.mamafua.data.remote.repository

import org.tridzen.mamafua.data.local.AppDatabase
import org.tridzen.mamafua.data.local.entities.Cart

class CartRepository(private val db: AppDatabase) : BaseRepository() {

    val cart = db.getCartDao().getCart()

    suspend fun insertEntry(cart: Cart) =
        db.getCartDao().insertEntry(cart)

    suspend fun updateEntry(count: Int, id: String) {
        db.getCartDao().updateEntry(count, id)
    }

    suspend fun removeEntry(id: String) =
        db.getCartDao().removeEntry(id)

    suspend fun insertCart(cart: List<Cart>) =
        db.getCartDao().insertCart(cart)

    suspend fun findCartItem(id: String): Cart? {
        return db.getCartDao().getCartItem(id)
    }

    suspend fun clearCart() = db.getCartDao().clearCart()
}
