package org.tridzen.mamafua.ui.home.launcher.get.services

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Service
import org.tridzen.mamafua.databinding.FragmentPackageBinding
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ServiceAdapter
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmodels.ServicesViewModel
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.visible

@AndroidEntryPoint
class PackageFragment : Fragment(R.layout.fragment_package) {

    private val servicesViewModel by viewModels<ServicesViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()

    private lateinit var binding: FragmentPackageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPackageBinding.bind(view)

        binding.lavItemized.visible(false)

        Coroutines.main {
            servicesViewModel.services.await().observe(viewLifecycleOwner, {
                if (!it.isNullOrEmpty()) {
                    val (match, _) = it.partition { service ->
                        service.description.trim().isNotEmpty()
                    }
                    initRecyclerView(match)
                    binding.lavItemized.visible(false)
                    runLayoutAnimation()
                }
            })
        }
    }

    private fun initRecyclerView(items: List<Service>) {
        binding.rvBouquet.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = ServiceAdapter(items, "Package", cartViewModel = cartViewModel)
        }
    }

    private fun runLayoutAnimation() = binding.rvBouquet.apply {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.list_animation)
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()
    }
}