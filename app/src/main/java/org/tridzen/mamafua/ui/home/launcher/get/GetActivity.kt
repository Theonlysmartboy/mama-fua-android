package org.tridzen.mamafua.ui.home.launcher.get

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.databinding.ActivityGetBinding
import org.tridzen.mamafua.ui.home.launcher.get.services.DeliveryFragment
import org.tridzen.mamafua.ui.home.launcher.get.services.ItemizedFragment
import org.tridzen.mamafua.ui.home.launcher.get.services.PackageFragment
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ViewPagerAdapter
import org.tridzen.mamafua.ui.home.order.OrderActivity
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel
import org.tridzen.mamafua.ui.home.order.profiles.FinalActivity

@AndroidEntryPoint
class GetActivity : AppCompatActivity() {

    private val cartViewModel by viewModels<CartViewModel>()

    private lateinit var binding: ActivityGetBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private var cart: List<Cart> = listOf()
    private val titles =
        arrayOf("Itemised", "Delivery", "Packages")
    private val fragments = arrayOf(
        ItemizedFragment() as Fragment,
        DeliveryFragment() as Fragment,
        PackageFragment() as Fragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGetBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewPagerAdapter = ViewPagerAdapter(this, titles = titles, fragments)
        binding.vpGet.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabs, binding.vpGet) { tab, position ->
            tab.text = titles[position]
        }.attach()

        cartViewModel.cart.observe(this, {
            if (!it.isNullOrEmpty()) {
                cart = it
                val item = it.first()

                when (item.style) {
                    "Itemized" -> {
                        disableTabs(1, 2)
                        selectPage(0)
                    }

                    "Machine" -> {
                        disableTabs(0, 2)
                        selectPage(1)
                    }

                    "Manual" -> {
                        disableTabs(0, 2)
                        selectPage(1)
                    }
                    else -> {
                        disableTabs(0, 1)
                        selectPage(2)
                    }
                }
            } else {
                enableTabs()
            }
        })

        binding.fabDone.setOnClickListener {
            if (cart.isNullOrEmpty()) {
                Snackbar.make(
                    binding.clGet,
                    "Your cart is empty, cannot proceed!",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else startActivity(Intent(this, FinalActivity::class.java))
        }


        setSupportActionBar(binding.tbGet)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_get, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.imCart -> {
                startActivity(Intent(this, OrderActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun disableTabs(vararg tabs: Int) {
        val tabStrip = binding.tabs.getChildAt(0) as LinearLayout
        for (tab in tabs)
            tabStrip.getChildAt(tab).setOnTouchListener { _, _ -> true }
        binding.vpGet.isUserInputEnabled = false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun enableTabs() {
        val tabStrip = binding.tabs.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnTouchListener { _, _ -> false }
        }
    }

    fun selectPage(pageIndex: Int) {
        binding.tabs.setScrollPosition(pageIndex, 0f, true)
        binding.vpGet.currentItem = pageIndex
    }
}