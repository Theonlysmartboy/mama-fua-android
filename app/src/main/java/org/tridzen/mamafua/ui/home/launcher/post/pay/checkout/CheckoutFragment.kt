package org.tridzen.mamafua.ui.home.launcher.post.pay.checkout

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_confirm.view.*
import kotlinx.android.synthetic.main.dialog_payments.view.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.coroutines.Job
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.data.local.entities.Payment
import org.tridzen.mamafua.databinding.DialogPaymentsBinding
import org.tridzen.mamafua.databinding.FragmentCheckoutBinding
import org.tridzen.mamafua.ui.home.launcher.post.prepare.cart.CartViewModel
import org.tridzen.mamafua.utils.*


@AndroidEntryPoint
class CheckoutFragment : Fragment(R.layout.fragment_checkout),
    OnPaymentListener {

    private val paymentsViewModel by viewModels<PaymentsViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()

    private lateinit var binding: FragmentCheckoutBinding

    private lateinit var modeAdapter: PaymentAdapter
    private lateinit var newMode: Payment
    private lateinit var payment: Payment
    private val paymentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.extras?.getBoolean("start") == true) {
                publishOrder()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCheckoutBinding.bind(view)

        paymentsViewModel.payments.observe(viewLifecycleOwner) {
            lavNoPayment.visible(it.isEmpty())
            tvNoPayment.visible(it.isEmpty())
            setUpRecyclerView(it)
        }

        binding.fabAdd.setOnClickListener {
            showBottomDialog({ payment ->
                paymentsViewModel.savePayment(payment)
            }, null)
        }

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rvWallet)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            paymentReceiver,
            IntentFilter(Constants.START_PAYMENT)
        );
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(paymentReceiver);
        super.onPause()
    }

    private fun setUpRecyclerView(list: List<Payment>) = binding.rvWallet.apply {
        modeAdapter = PaymentAdapter(list, this@CheckoutFragment)
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        adapter = modeAdapter
        val layoutManager = layoutManager as LinearLayoutManager
        val visiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (visiblePosition > -1) {
            payment = list[visiblePosition]
            Log.d("Position 2, number 2", "${visiblePosition}${payment.number}")
        }
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
//                        val firstVisiblePosition =
//                            layoutManager.findFirstCompletelyVisibleItemPosition()
//                        if (firstVisiblePosition > -1) {
//                            payment = list[firstVisiblePosition]
//                            Log.d("Position, number", "${firstVisiblePosition}${payment.number}")
//                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (visiblePosition > -1) {
                    payment = list[visiblePosition]
                    Log.d("Position 2, number 2", "$visiblePosition ${payment.number}")
                }
            }
        })
    }

    private fun showBottomDialog(
        editFunction: ((payment: Payment) -> Job),
        payment: Payment? = null
    ) {
        val sheet = DialogPaymentsBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.setContentView(sheet.root)
        val cancel = sheet.butDismiss
        val apply = sheet.butApply
        val fieldOne = sheet.tetNumber
        val fieldTwo = sheet.tetConfirm
        val fieldName = sheet.tetName

        payment?.let {
            fieldOne.setText(payment.number)
            fieldTwo.setText(payment.number)
            fieldName.setText(payment.name)
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        apply.setOnClickListener {
            val numOne = fieldOne.text.toString()
            val numTwo = fieldTwo.text.toString()
            val name = fieldName.text.toString()
            if (numOne.length < 10) {
                fieldOne.error = "Number is invalid"
                return@setOnClickListener
            }

            if (numTwo.length < 10) {
                fieldTwo.error = "Number is invalid"
                return@setOnClickListener
            }

            if (numOne != numTwo) {
                fieldTwo.error = "Numbers don't match"
                return@setOnClickListener
            }
            if (name.trim().isEmpty()) {
                fieldName.error = "Name cannot be empty"
                return@setOnClickListener
            }

            if (numOne.validateNumber()) {
                fieldOne.error = null
                newMode = Payment(
                    name = name,
                    number = numTwo,
                    orders = listOf()
                )
                editFunction.invoke(newMode)
                dialog.dismiss()
            } else {
                fieldOne.error = "Please enter safaricom numbers only"
                return@setOnClickListener
            }
        }

        dialog.show()
    }

    override fun deletePayment(payment: Payment) {
        paymentsViewModel.deleteMode(payment.number)
    }

    override fun editPayment(payment: Payment) {
        showBottomDialog({ payment1 ->
            paymentsViewModel.editPayment(payment1.number, payment1.name, payment.id)
        }, payment)
    }

    private fun publishOrder() {
        val list = mutableListOf<Cart>()
        cartViewModel.cart.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                list.addAll(it)
            }
        }
        val order = Order()
        val old = payment.number
        val new = old.replaceFirst("0", "254")
        order.apply {
            services = list
            amount = 1000
            center = "Ngong"
            deliveryDate = "28548753825"
            fullfillerId = "5ff37db52e132830349fe57e"
            fullfillerName = "Jane Doe"
            longitude = 23.23868
            latitude = 4.36863
            paidVia = "Mpesa"
            phone = new
            status = "Paid"
            placedBy = "600e7b8c889c8b2284d2309a"
            transactionId = ""
        }
        val dialog = requireContext().showMaterialDialog(
            "Confirm your number again!",
            "Accepting this prompt will initiate stk payment via M-Pesa. You will be presented with the lipa na mpesa dialog on your handest and you will be required to enter your pin. After the payment of ${2000} Ksh is processed, your order will be sent to our servers and the provider you chose will start working on the order",
            buttonConfirm = "Accept",
            buttonCancel = "Cancel",
            function = { paymentsViewModel.makePayment(order) }
        )
        dialog.cancelable(false)
        dialog.show()
    }
}