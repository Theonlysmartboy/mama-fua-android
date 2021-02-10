package org.tridzen.mamafua.ui.home.launcher.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.databinding.ActivityLoyaltyBinding
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ViewPagerAdapter
import org.tridzen.mamafua.ui.home.launcher.history.activity.HistoryFragment
import org.tridzen.mamafua.ui.home.launcher.history.news.NewsFragment

@AndroidEntryPoint
class LoyaltyActivity : AppCompatActivity() {

    private val fragments = arrayOf(HistoryFragment() as Fragment, NewsFragment() as Fragment)
    private val titles = arrayOf("Activity", "News")
    private lateinit var binding: ActivityLoyaltyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoyaltyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.vpLoyalty.adapter = ViewPagerAdapter(this, titles, fragments)
        TabLayoutMediator(binding.tabLayout, binding.vpLoyalty) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}