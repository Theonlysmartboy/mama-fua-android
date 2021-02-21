package org.tridzen.mamafua.ui.home.launcher.get.services

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Service
import org.tridzen.mamafua.databinding.FragmentDeliveryBinding
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ServiceAdapter
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmodels.ServicesViewModel
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.visible

@AndroidEntryPoint
class DeliveryFragment : Fragment(R.layout.fragment_delivery), View.OnClickListener {

    private val servicesViewModel by viewModels<ServicesViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()

    private lateinit var binding: FragmentDeliveryBinding
    private var img: Drawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDeliveryBinding.bind(view)

        binding.lavItemized.visible(false)

        img = ContextCompat.getDrawable(requireContext(), R.drawable.ic_current)
        setUpButtons()

        Coroutines.main {
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

    private fun initRecyclerView(items: List<Service>) {
        binding.rvResidential.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            servicesViewModel.washMode.observe(viewLifecycleOwner) {
                adapter = ServiceAdapter(items, "Delivery", it, cartViewModel)
                switchViews(it)
            }
        }
    }

    private fun runLayoutAnimation() = binding.rvResidential.apply {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_animation)
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()
    }

    override fun onClick(v: View?) {
        servicesViewModel.switchWashMode()
    }

    private fun setUpButtons() {
        binding.materialButtonToggleGroupSort.isSingleSelection = true
        binding.materialButtonToggleGroupSort.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (isChecked) {
                true -> servicesViewModel.switchWashMode()
            }
        }
    }

    private fun switchViews(selected: String) {
        when (selected) {
            "Machine" -> {
            }

            "Manual" -> {
            }
        }
    }
}