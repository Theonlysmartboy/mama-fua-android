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

    private lateinit var fragments: Array<Fragment>
    private lateinit var titles: Array<String>
    private lateinit var binding: ActivityLoyaltyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoyaltyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragments = arrayOf(HistoryFragment() as Fragment, NewsFragment() as Fragment)
        titles = arrayOf("Activity", "News")

        binding.vpLoyalty.adapter = ViewPagerAdapter(this, titles, fragments)
        TabLayoutMediator(binding.tabLayout, binding.vpLoyalty) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}