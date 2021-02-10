package org.tridzen.mamafua.ui.home.launcher.post.prepare.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.data.remote.repository.CartRepository
import org.tridzen.mamafua.utils.base.BaseViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : BaseViewModel(cartRepository) {

    private var _cart: LiveData<MutableList<Cart>> = MutableLiveData()
    val cart: LiveData<MutableList<Cart>>
        get() = _cart

    init {
        _cart = cartRepository.cart
        cart.observeForever {
            for (cart in it) {
                if (cart.count == 0) {
                    Coroutines.io {
                        cartRepository.removeEntry(cart)
                    }
                }
            }
        }
    }

    fun insertEntry(cartItem: Cart) {
        Coroutines.main {
            val cart = cartRepository.findCartItem(cartItem.id)
            if (cart == null) {
                cartItem.count++
                cartRepository.insertEntry(cartItem)
            } else {
                var count = cart.count
                count++
                cartRepository.updateEntry(count, cart.id)
            }
        }
    }

    fun deleteEntry(cartItem: Cart) {
        Coroutines.main {
            val cart = cartRepository.findCartItem(cartItem.id)
            if (cart != null) {
                if (cart.count > 0) {
                    var count = cart.count
                    count--
                    cartRepository.updateEntry(count, cart.id)
                } else if (cart.count <= 0) {
                    cartRepository.removeEntry(cart)
                }
            }
        }
    }

    fun removeEntry(cartItem: Cart) = Coroutines.main {
        val cart = cartRepository.findCartItem(cartItem.id)
        if (cart != null) {
            cartRepository.removeEntry(cart)
        }
    }

    fun clearCart() = Coroutines.io {
        cartRepository.clearCart()
    }
}