package org.tridzen.mamafua.ui.home.launcher.post.profiles

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.databinding.FragmentProfilesBinding
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.data.Prefs
import org.tridzen.mamafua.utils.runLayoutAnimation
import javax.inject.Inject


@AndroidEntryPoint
class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val profilesViewModel by viewModels<ProfilesViewModel>()

    @Inject
    lateinit var prefs: Prefs

    private lateinit var binding: FragmentProfilesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfilesBinding.bind(view)

//        binding.cgProfiles.setOnCheckedChangeListener { _, _ ->
//            val list = binding.cgProfiles.children
//                .toList()
//                .filter { (it as Chip).isChecked }
//                .joinToString(", ") { (it as Chip).text }
//
//            if (list == "All") fetchData()
//
//            profilesViewModel.setCenterId(list)
//        }
//
//        profilesViewModel.centerId.observe(viewLifecycleOwner) {
//            fetchProfilesById(profilesViewModel)
//        }
//
//        binding.srlProfiles.setOnRefreshListener {
//            fetchData()
//            binding.srlProfiles.isRefreshing = false
//        }

        fetchData()
        setUp()
    }

    private fun setUp(){
        binding.backdropLayout.frontSheet = binding.frontLayer
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_add)?.let {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_minus)?.let { it1 ->
                binding.backdropLayout.setTriggerView(binding.trigger,
                    it, it1
                )
            }
        }

// The triggerView can be any view that should trigger the backdrop toggle()
// Toggle backdrop on trigger view click

    }

    private fun setUpRecyclerView(list: List<Profile>) = binding.rvProfiles.apply {
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val profilesAdapter = ProfilesAdapter(list, prefs, findNavController())
        adapter = profilesAdapter
        runLayoutAnimation()
    }

    private fun fetchData() = Coroutines.main {
        profilesViewModel.profiles.await().observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                }
                is Resource.Success -> setUpRecyclerView(it.value.profiles)
            }
        })
    }

    private fun fetchProfilesById(viewModel: ProfilesViewModel) = Coroutines.main {
        viewModel.profilesByCenter.await()?.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                }
                is Resource.Success -> setUpRecyclerView(it.value.profiles)
            }
        })
    }
}