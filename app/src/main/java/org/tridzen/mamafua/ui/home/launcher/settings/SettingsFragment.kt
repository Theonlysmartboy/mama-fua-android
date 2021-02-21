package org.tridzen.mamafua.ui.home.launcher.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.User
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.databinding.FragmentSettingsBinding
import org.tridzen.mamafua.ui.home.HomeViewModel
import org.tridzen.mamafua.ui.home.interfaces.OnLogoutHandler
import org.tridzen.mamafua.ui.onboarding.OnBoardingActivity
import org.tridzen.mamafua.utils.firstLetter
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings),
    OnLogoutHandler {

    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        lifecycleScope.launch {
            homeViewModel.getLoggedInUser.await().observe(viewLifecycleOwner) {
                if (it != null)
                    updateUI(it)
            }
        }

        val list = mutableListOf<Settings>()
        val settingsAdapter = SettingsAdapter(list)
        settingsAdapter.onLogoutHandler = this

        populateList(list)

        binding.rvSettings.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = settingsAdapter
            val dividerItemDecoration: ItemDecoration =
                DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.divider)!!)
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun updateUI(user: User) {
        binding.tvAvatar.text = firstLetter(user.username)
        binding.tvName.setText(user.username)
        binding.tvPhone.setText(user.phone)
    }

    override fun onLogout(logout: Boolean) {
        if (logout) {
            lifecycleScope.launch {
                appPreferences.clear()
            }
            startActivity(Intent(requireContext(), OnBoardingActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun populateList(list: MutableList<Settings>) {
        list.add(
            Settings(
                "Theme",
                arrayListOf("Dark", "Light"),
                R.drawable.ic_theme,
                "",
                null,
                true
            )
        )

        list.add(
            Settings(
                "Promo code dialog",
                arrayListOf("Always show", "Don't show"),
                R.drawable.ic_badge,
                "",
                null,
                true
            )
        )

        list.add(
            Settings(
                "Terms & conditions",
                null,
                R.drawable.ic_terms,
                "This will log you out of this session and clear all local data you've saved. Online data such as your profile will remain untouched",
                arrayListOf("Unread", "Read"),
                null
            )
        )
        list.add(
            Settings(
                "Privacy policy",
                null,
                R.drawable.ic_privacy,
                "This will log you out of this session and clear all local data you've saved. Online data such as your profile will remain untouched",
                arrayListOf("Unread", "Read"),
                null
            )
        )
        list.add(
            Settings(
                "Logout",
                null,
                R.drawable.ic_logout,
                "This will log you out of this session and clear all local data you've saved.\nOnline data such as your profile and previous activity will remain untouched",
                arrayListOf("Confirm", "Cancel"),
                null
            )
        )
    }
}

