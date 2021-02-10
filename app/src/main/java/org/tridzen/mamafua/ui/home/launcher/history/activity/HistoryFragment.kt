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
import org.tridzen.mamafua.databinding.FragmentPointsBinding
import org.tridzen.mamafua.ui.home.HomeViewModel
import org.tridzen.mamafua.ui.home.OnUserIdFound
import org.tridzen.mamafua.ui.home.launcher.post.OrdersViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.hideView
import org.tridzen.mamafua.utils.runLayoutAnimation

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_points),
    OnUserIdFound {

    private val ordersViewModel by viewModels<OrdersViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var historyAdapter: HistoryAdapter
    private val onUserIdFound: OnUserIdFound = this

    private lateinit var binding: FragmentPointsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPointsBinding.bind(view)

        Coroutines.main {
            homeViewModel.getLoggedInUser.await().observe(viewLifecycleOwner) {
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
            viewLifecycleOwner.let {
                ordersViewModel.theOrders(id).observe(it) { list ->
                    hideView(binding.lavActivity, binding.mtvActivity, condition = list.isEmpty())
                    setUpRv(list)
                }
            }
        }
    }
}