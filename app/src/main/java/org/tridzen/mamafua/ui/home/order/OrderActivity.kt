package org.tridzen.mamafua.ui.home.order

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Payment
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.LATITUDE_PREFS
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.LONGITUDE_PREFS
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.TIME_PREFS
import org.tridzen.mamafua.databinding.ActivityOrderBinding
import org.tridzen.mamafua.ui.home.interfaces.OnPaymentListener
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel
import org.tridzen.mamafua.ui.home.order.prepare.checkout.PaymentsViewModel
import org.tridzen.mamafua.ui.home.order.profiles.FinalActivity
import org.tridzen.mamafua.ui.home.order.profiles.viewmodels.ProfilesViewModel
import org.tridzen.mamafua.utils.Constants
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.enable
import org.tridzen.mamafua.utils.showMaterialDialog
import javax.inject.Inject

@AndroidEntryPoint
class OrderActivity : AppCompatActivity(), OnPaymentListener {
    private lateinit var navController: NavController

    private val cartViewModel by viewModels<CartViewModel>()
    private val profilesViewModel by viewModels<ProfilesViewModel>()
    private val paymentsViewModel by viewModels<PaymentsViewModel>()

    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var navOptions: NavOptions
    private lateinit var binding: ActivityOrderBinding

    @Inject
    lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.fragment5)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        navOptions = NavOptions.Builder().setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

        Coroutines.main {
            binding.butConfirm.enable(checkForParams()?.first != null)
            binding.butConfirm.enable(checkForParams()?.second != null)
            binding.butConfirm.enable(checkForParams()?.third != null)

            showSnackBar(checkForParams()?.first == null)
        }

        paymentsViewModel.payments.observe(this) {
            if (it.isEmpty())
                binding.butConfirm.enable(false)
            else binding.butConfirm.enable(true)
        }

        binding.tlOrder.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
                        binding.butConfirm.enable(false)
                        binding.butClear.enable(false)
                    }
                }
            }

            R.id.reviewFragment -> {
                binding.butConfirm.enable(false)
            }

            R.id.checkoutFragment -> {
                paymentsViewModel.payments.observe(this) {
                    binding.butConfirm.enable(it.isNotEmpty())
                }
            }
        }

        binding.butConfirm.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.cartFragment -> {
                    navController.navigate(
                        R.id.action_cartFragment_to_mapsFragment,
                        null,
                        navOptions
                    )
                    binding.tlOrder.setScrollPosition(1, 0f, true)
                }

                R.id.reviewFragment -> {
                    navController.navigate(
                        R.id.action_mapsFragment_to_checkoutFragment,
                        null,
                        navOptions
                    )
                    binding.tlOrder.setScrollPosition(2, 0f, true)
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
        val tabLayout = (binding.tlOrder.getChildAt(0) as ViewGroup).getChildAt(
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
                binding.tlOrder.setScrollPosition(1, 0f, true)
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
                binding.tlOrder.setScrollPosition(1, 0f, true)
            }
            R.id.reviewFragment -> {
                navController.navigate(
                    R.id.action_reviewFragment_to_cartFragment,
                    null,
                    null
                )
                binding.tlOrder.setScrollPosition(0, 0f, true)
            }
            else -> finish()
        }
    }

    override fun deletePayment(payment: Payment) {

    }

    override fun editPayment(payment: Payment) {
    }

    private suspend fun checkForParams(): Triple<String?, Double?, Double?>? =
        withContext(Dispatchers.Main) {
            val time: String? = prefs.getValue(TIME_PREFS).first()
            val lat: Double? = prefs.getValue(LATITUDE_PREFS).first()
            val long: Double? = prefs.getValue(LONGITUDE_PREFS).first()

            Log.d("Params", Triple(time, lat, long).toString())
            if (time == null || lat == null || long == null) null else Triple(time, lat, long)
        }


    private fun showSnackBar(show: Boolean) {
        val snackbar = Snackbar.make(
            binding.root,
            "You have not set time, location and service provider.",
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction("SET") { startActivity(Intent(this, FinalActivity::class.java)) }
        if (show) snackbar.show()
    }
}