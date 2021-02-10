package org.tridzen.mamafua.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.User
import org.tridzen.mamafua.databinding.FragmentHomeBinding
import org.tridzen.mamafua.ui.home.launcher.post.OrderActivity
import org.tridzen.mamafua.utils.firstLetter

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.cdOne.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_getActivity)
        }

        binding.cdTwo.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_applyFragment)
        }

        binding.cdThree.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loyaltyActivity)
        }

        binding.cdFour.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        binding.ivCart.setOnClickListener {
            startActivity(Intent(requireContext(), OrderActivity::class.java))
        }

        lifecycleScope.launch {
            homeViewModel.getLoggedInUser.await().observe(viewLifecycleOwner) {
                if (it != null)
                    updateUI(it)
            }
        }
    }

    private fun updateUI(user: User) {
        binding.tvAvatar.text = firstLetter(user.username)
        binding.tvPoints.text = "${user.points} Points"
        binding.tvProfile.text = user.username
    }
}