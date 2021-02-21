package org.tridzen.mamafua.ui.home.order

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Payment
import org.tridzen.mamafua.ui.home.interfaces.OnPaymentListener
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel
import org.tridzen.mamafua.ui.home.order.prepare.checkout.PaymentsViewModel
import org.tridzen.mamafua.ui.home.order.profiles.viewmodels.ProfilesViewModel
import org.tridzen.mamafua.utils.Constants
import org.tridzen.mamafua.utils.enable
import org.tridzen.mamafua.utils.showMaterialDialog

@AndroidEntryPoint
class OrderActivity : AppCompatActivity(), OnPaymentListener {
    private lateinit var navController: NavController

    private val cartViewModel by viewModels<CartViewModel>()
    private val profilesViewModel by viewModels<ProfilesViewModel>()
    private val paymentsViewModel by viewModels<PaymentsViewModel>()

    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var navOptions: NavOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        navController = Navigation.findNavController(this, R.id.fragment5)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        navOptions = NavOptions.Builder().setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

        tlOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeTabText(tab!!.position)

                when (tab.position) {
                    0 -> navController.navigate(R.id.cartFragment, null, navOptions)
                    1 -> navController.navigate(R.id.reviewFragment, null, navOptions)
                    2 -> navController.navigate(R.id.checkoutFragment, null, navOptions)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                changeTabText(tab!!.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                changeTabText(tab!!.position)
            }
        })

        when (navController.currentDestination?.id) {
            R.id.cartFragment -> {
                cartViewModel.cart.observe(this) {
                    if (it.isEmpty()) {
                        butConfirm.enable(false)
                        butClear.enable(false)
                    }
                }
            }

            R.id.reviewFragment -> {
                butConfirm.enable(false)
            }

            R.id.checkoutFragment -> {
                paymentsViewModel.payments.observe(this) {
                    if (it.isEmpty()) {
                        butConfirm.enable(false)
                    }
                }
            }
        }

        butConfirm.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.cartFragment -> {
                    navController.navigate(
                        R.id.action_cartFragment_to_mapsFragment,
                        null,
                        navOptions
                    )
                    tlOrder.setScrollPosition(1, 0f, true)
                }

                R.id.reviewFragment -> {
                    navController.navigate(
                        R.id.action_mapsFragment_to_checkoutFragment,
                        null,
                        navOptions
                    )
                    tlOrder.setScrollPosition(2, 0f, true)
                }

                R.id.checkoutFragment -> {
                    val localIntent = Intent(Constants.START_PAYMENT)
                        .putExtra("start", true)
                    localBroadcastManager.sendBroadcast(localIntent)
                }
            }
        }

        butClear.setOnClickListener {
            showMaterialDialog(
                titleText = "Confirmation required!",
                contentText = "This will clear your order from the queue. \n" + "All items and the provider will be removed. \n" + "This action cannot be undone",
                function = { cartViewModel.clearCart() }
            )
        }
    }

    private fun changeTabText(position: Int) {
        val tabLayout = (tlOrder.getChildAt(0) as ViewGroup).getChildAt(
            position
        ) as LinearLayout
        val tabTextView = tabLayout.getChildAt(1) as TextView
        tabTextView.setTypeface(tabTextView.typeface, Typeface.BOLD)
    }

    override fun onSupportNavigateUp(): Boolean {
        return when (navController.currentDestination?.id) {
            R.id.checkoutFragment -> {
                navController.navigate(
                    R.id.action_checkoutFragment_to_reviewFragment,
                    null,
                    null
                )
                tlOrder.setScrollPosition(1, 0f, true)
                true
            }
            else -> navController.navigateUp()
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.checkoutFragment -> {
                navController.navigate(
                    R.id.action_checkoutFragment_to_reviewFragment,
                    null,
                    null
                )
                tlOrder.setScrollPosition(1, 0f, true)
            }
            R.id.reviewFragment -> {
                navController.navigate(
                    R.id.action_reviewFragment_to_cartFragment,
                    null,
                    null
                )
                tlOrder.setScrollPosition(0, 0f, true)
            }
            else -> finish()
        }
    }

    override fun deletePayment(payment: Payment) {

    }

    override fun editPayment(payment: Payment) {
    }
}