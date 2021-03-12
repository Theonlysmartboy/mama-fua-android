package org.tridzen.mamafua.ui.home.order.prepare.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.databinding.FragmentReviewBinding
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private val viewModel by viewModels<CartViewModel>()
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private var total: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.cart.observe(viewLifecycleOwner, {
            if (it != null) {
                setUpRv(it)
                it.forEach { cart ->
                    total += (getPrice(cart) * cart.count)
                }

                binding.tvTotalFees.text = total.toString()
            }
        })
    }

    private fun setUpRv(list: List<Cart>) {
        val reviewAdapter = ReviewAdapter()
        reviewAdapter.submitList(list)
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = reviewAdapter
        }
    }
    private fun getPrice(cart: Cart): Int {
        return when (cart.style) {
            "Itemized" -> cart.service.offSitePrice
            "Package" -> cart.service.offSitePrice
            "Manual" -> cart.service.onSitePrice
            else -> cart.service.machinePrice
        }
    }
}