package org.tridzen.mamafua.ui.home.order.prepare.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cart.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.data.local.entities.Params
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.databinding.FragmentCartBinding
import org.tridzen.mamafua.ui.home.launcher.get.GetActivity
import org.tridzen.mamafua.ui.home.order.profiles.viewmodels.ProfilesViewModel
import org.tridzen.mamafua.utils.base.SwipeToDeleteCallback
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.visible
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val cartViewModel by activityViewModels<CartViewModel>()
    private val profilesViewModel by activityViewModels<ProfilesViewModel>()

    private lateinit var binding: FragmentCartBinding
    private var cartList: MutableList<Cart> = mutableListOf()
    private var paramsList: MutableList<Params> = mutableListOf()
    private var profile: Profile? = null
//    private var latitude: Double? = null
//    private var longitude: Double? = null
//    private val latLng: LatLng by lazy {
//        LatLng(latitude, longitude)
//    }
//    private lateinit var date: String

    private var cartAdapter: CartAdapter? = null
    private var paramsAdapter: ParamsAdapter? = null

    @Inject
    lateinit var prefs: AppPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)

        cartViewModel.cart.observe(viewLifecycleOwner, {
            cartList.clear()
            cartList.addAll(it)
            setUpRv(it)

            setUpParams()

            lavEmptyCart.visible(it.isEmpty())
            tvEmptyCart.visible(it.isEmpty())
            fabEmpty.visible(it.isEmpty())
        })

        fabEmpty.setOnClickListener {
            startActivity(Intent(requireContext(), GetActivity::class.java))
        }

        cartAdapter = CartAdapter()
        cartAdapter?.cartViewModel = cartViewModel

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                cartViewModel.removeEntry(cartList[viewHolder.adapterPosition])
                cartList.remove(cartList[viewHolder.adapterPosition])
                cartAdapter?.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)
    }

    private fun setUpRv(list: List<Cart>) {
        cartAdapter?.submitList(list)
        binding.rvCart.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
        }
    }

    private fun setUpRv2(list: List<Params>) {
        paramsAdapter = ParamsAdapter()
        paramsAdapter?.submitList(list)
        binding.rvParams.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = paramsAdapter
        }
        binding.rvParams.onFlingListener = null
        PagerSnapHelper().attachToRecyclerView(binding.rvParams)
    }

    private fun setUpParams() = Coroutines.main {
        paramsList.clear()
        paramsList.add(Params("Time", Profile()))
        paramsList.add(Params("Location", Profile()))

        profilesViewModel.getProfile().observe(viewLifecycleOwner) { saved ->
            if (saved != null) {
                profile = saved
                paramsList.add(Params("Profile", saved))
                setUpRv2(paramsList)
            }
        }
    }

    data class LatLng(var latitude: Double = 0.0, var longitude: Double = 0.0) {
        constructor() : this(0.0, 0.0)
    }
}