package org.tridzen.mamafua.ui.home.launcher.post.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.databinding.FragmentProfilesBinding
import org.tridzen.mamafua.utils.base.OnBottomSheetCallbacks
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.data.Prefs
import org.tridzen.mamafua.utils.runLayoutAnimation
import javax.inject.Inject


@AndroidEntryPoint
class ProfilesFragment : BottomSheetDialogFragment(), OnBottomSheetCallbacks {

    private val profilesViewModel by viewModels<ProfilesViewModel>()

    @Inject
    lateinit var prefs: Prefs
    private var currentState: Int = BottomSheetBehavior.STATE_EXPANDED

    private var _binding: FragmentProfilesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textResult.setOnClickListener {
            (activity as FinalActivity).openBottomSheet()
        }

        binding.filterImage.setOnClickListener {
            when (currentState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    (activity as FinalActivity).closeBottomSheet()
                }
                else -> {
                    (activity as FinalActivity).openBottomSheet()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        (activity as FinalActivity).setOnBottomSheetCallbacks(this)
        _binding = FragmentProfilesBinding.inflate(inflater, container, false)

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentProfilesBinding.bind(view)
//
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
//
//        fetchData()
//        setUp()
//    }

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

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        currentState = newState
        val scale = resources.displayMetrics.density
        val dpAsPixels = (24 * scale + 0.5f)
        when (newState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                binding.textResult.text = "0 results"
                binding.filterImage.setImageResource(R.drawable.ic_baseline_filter_list_24)
                binding.cdlProfiles.setPadding(0, dpAsPixels.toInt(), 0, 0)
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                binding.textResult.text = "See the results"
                binding.filterImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                binding.cdlProfiles.setPadding(0, 0, 0, 0)
            }
        }
    }
}