package org.tridzen.mamafua.ui.home.launcher.history.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.data.local.entities.User
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.databinding.FragmentHistoryBinding
import org.tridzen.mamafua.ui.home.HomeViewModel
import org.tridzen.mamafua.ui.home.interfaces.OnUserIdFound
import org.tridzen.mamafua.ui.home.order.OrdersViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.hideView
import org.tridzen.mamafua.utils.runLayoutAnimation

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history), OnUserIdFound {

    private val ordersViewModel by viewModels<OrdersViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var historyAdapter: HistoryAdapter
    private var id: String? = ""
    private var user: User? = null

    private lateinit var binding: FragmentHistoryBinding
    private var onUserIdFound: OnUserIdFound = this

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)

        Coroutines.main {
            homeViewModel.getLoggedInUser.await().observe(viewLifecycleOwner) {
                id = it._id
                user = it
                Log.d("Theuser", user.toString())
                onUserIdFound.userIdFound(it._id)
            }
        }
    }

    private fun setUpRv(list: List<Order>) = binding.rvM.apply {
        historyAdapter = HistoryAdapter(list)
        historyAdapter.setExpanded(false)
        adapter = historyAdapter
        addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        runLayoutAnimation()
    }

    override fun userIdFound(id: String) {
        Coroutines.main {
            Log.d("Theuserid", id)
            ordersViewModel.theOrders(id).observe(viewLifecycleOwner) { list ->
                when (list) {
                    is Resource.Failure -> {
                    }
                    Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val orders = list.value.orders
                        hideView(
                            binding.lavActivity,
                            binding.mtvActivity,
                            condition = orders.isEmpty()
                        )
                        setUpRv(orders)
                    }
                }
            }
        }
    }
}