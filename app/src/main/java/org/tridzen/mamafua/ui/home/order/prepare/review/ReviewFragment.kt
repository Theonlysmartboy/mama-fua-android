package org.tridzen.mamafua.ui.home.order.prepare.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_referral.view.*
import kotlinx.coroutines.flow.first
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.databinding.FragmentReviewBinding
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines

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

        Coroutines.main {
            val show = AppPreferences(view.context).getValue(AppPreferences.SHOW_REFERRALS).first()
            if (show == true) showBottomDialog()
        }

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

    private fun showBottomDialog() {
        val sheet = layoutInflater.inflate(R.layout.dialog_referral, null)
        val dialog = BottomSheetDialog(this.requireActivity())
        dialog.setContentView(sheet)
        val cancel = sheet.butCancel
        val apply = sheet.butApply
        val code = sheet.tetReferral.text.toString()

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        apply.setOnClickListener {

        }

        dialog.show()
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