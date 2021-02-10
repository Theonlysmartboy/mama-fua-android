package org.tridzen.mamafua.ui.home.launcher.get.services

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Service
import org.tridzen.mamafua.databinding.FragmentItemizedBinding
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ServiceAdapter
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmodels.ServicesViewModel
import org.tridzen.mamafua.ui.home.launcher.post.prepare.cart.CartViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.visible

@AndroidEntryPoint
class ItemizedFragment : Fragment(R.layout.fragment_itemized) {

    private val servicesViewModel by viewModels<ServicesViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()

    private lateinit var binding: FragmentItemizedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemizedBinding.bind(view)

        fetchData()
    }

    private fun initRecyclerView(items: List<Service>) {
        binding.rvResidential.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = ServiceAdapter(items, "Itemized", cartViewModel = cartViewModel)
        }
    }

    private fun runLayoutAnimation() = binding.rvResidential.apply {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_animation)
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()
    }

    private fun fetchData() = Coroutines.main {
        servicesViewModel.services.await().observe(viewLifecycleOwner, {
            val (_, rest) = it.partition { service ->
                service.description.trim().isNotEmpty()
            }
            initRecyclerView(rest)
            binding.lavItemized.visible(false)
            runLayoutAnimation()
        })
    }
}