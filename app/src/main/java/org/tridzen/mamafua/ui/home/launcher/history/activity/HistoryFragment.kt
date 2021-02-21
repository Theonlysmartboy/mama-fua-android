package org.tridzen.mamafua.ui.home.launcher.history.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.databinding.FragmentHistoryBinding
import org.tridzen.mamafua.ui.home.HomeViewModel
import org.tridzen.mamafua.ui.home.interfaces.OnUserIdFound
import org.tridzen.mamafua.ui.home.order.OrdersViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.hideView
import org.tridzen.mamafua.utils.runLayoutAnimation

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history),
    OnUserIdFound {

    private val ordersViewModel by viewModels<OrdersViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var historyAdapter: HistoryAdapter
    private val onUserIdFound: OnUserIdFound = this
    private lateinit var id: String

    private lateinit var binding: FragmentHistoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)

        Coroutines.main {
            homeViewModel.getLoggedInUser.await().observe(viewLifecycleOwner) {
                id = it._id
                Coroutines.main {
                    ordersViewModel.theOrders(it._id).observe(viewLifecycleOwner) { list ->
                        hideView(
                            binding.lavActivity,
                            binding.mtvActivity,
                            condition = list.isEmpty()
                        )
                        setUpRv(list)
                    }
                }
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

    }
}