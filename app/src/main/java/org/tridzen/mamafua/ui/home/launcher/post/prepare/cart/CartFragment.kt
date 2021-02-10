package org.tridzen.mamafua.ui.home.launcher.post.prepare.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cart.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.databinding.FragmentCartBinding
import org.tridzen.mamafua.ui.home.launcher.get.GetActivity
import org.tridzen.mamafua.utils.base.SwipeToDeleteCallback
import org.tridzen.mamafua.utils.visible

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val viewModel by viewModels<CartViewModel>()

    private lateinit var binding: FragmentCartBinding

    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: MutableList<Cart>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)

        viewModel.cart.observe(viewLifecycleOwner, {
            cartList = it
            setUpRv(cartList)

            lavEmptyCart.visible(it.isEmpty())
            tvEmptyCart.visible(it.isEmpty())
            fabEmpty.visible(it.isEmpty())
        })

        fabEmpty.setOnClickListener {
            startActivity(Intent(requireContext(), GetActivity::class.java))
        }

        cartAdapter = CartAdapter()
        cartAdapter.cartViewModel = viewModel

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removeEntry(cartList[viewHolder.adapterPosition])
                cartList.remove(cartList[viewHolder.adapterPosition])
                cartAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)
    }

    private fun setUpRv(list: List<Cart>) {
        cartAdapter.submitList(list)
        binding.rvCart.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
        }
    }
}