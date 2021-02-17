package org.tridzen.mamafua.ui.home.launcher.post.profiles.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Center
import org.tridzen.mamafua.data.remote.network.current.Resource
import org.tridzen.mamafua.databinding.FragmentFilterBinding
import org.tridzen.mamafua.ui.home.launcher.post.profiles.CentersViewModel
import org.tridzen.mamafua.ui.home.launcher.post.profiles.ProfilesViewModel
import org.tridzen.mamafua.utils.coroutines.Coroutines

@AndroidEntryPoint
class FilterFragment : Fragment(R.layout.fragment_filter) {

    private val centersViewModel by viewModels<CentersViewModel>()
    private val profilesViewModel by activityViewModels<ProfilesViewModel>()

    private lateinit var binding: FragmentFilterBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterBinding.bind(view)

        fetchCenters()
    }

    private fun fetchCenters() = Coroutines.main {
        centersViewModel.centers.await().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                }
                is Resource.Success -> {
                    setUpChips(it.value.centers)
                }
            }
        }
    }

    private fun setUpChips(list: List<Center>) {
        binding.cgProfiles.removeAllViews()

        for (center in list) {
            val chip = Chip(requireContext())
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Filter
            )

            chipDrawable.setCheckedIconTintResource(R.color.white)
            chip.text = center.name
            chip.id = ViewCompat.generateViewId()
            chip.setChipDrawable(chipDrawable)

            binding.cgProfiles.addView(chip)
        }

        binding.cgProfiles.setOnCheckedChangeListener { group, _ ->
            val chips = group.children
                .toList()
                .filter { (it as Chip).isChecked }
                .joinToString(", ") { (it as Chip).text }

            profilesViewModel.getData(chips)
        }
    }
}