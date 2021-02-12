package org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fa: FragmentActivity,
    private val titles: Array<String>,
    private val fragments: Array<Fragment>
) : FragmentStateAdapter(fa) {

    override fun getItemCount() = titles.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}