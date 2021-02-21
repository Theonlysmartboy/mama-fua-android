package org.tridzen.mamafua.ui.home.order.profiles.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.TIME_PREFS
import org.tridzen.mamafua.databinding.FragmentDatetimeBinding
import org.tridzen.mamafua.ui.home.order.OrdersViewModel
import org.tridzen.mamafua.ui.home.order.profiles.FinalActivity
import org.tridzen.mamafua.utils.coroutines.Coroutines

@AndroidEntryPoint
class DateTimeFragment : Fragment(R.layout.fragment_datetime) {

    private val ordersViewModel by viewModels<OrdersViewModel>()

    private val appPrefs by lazy {
        AppPreferences(requireContext())
    }

    private lateinit var binding: FragmentDatetimeBinding
    private lateinit var selectedDate: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDatetimeBinding.bind(view)
        binding.sdtPicker.addOnDateChangedListener { displayed, date ->
            selectedDate = date.toString()
        }

        binding.butOk.setOnClickListener {
            try {
                (activity as FinalActivity).onNavigateListener.onDateSelected(selectedDate)
            } catch (e: UninitializedPropertyAccessException) {
                selectedDate = LocalDateTime.now().format(
                    DateTimeFormatter.ofLocalizedDateTime(
                        FormatStyle.FULL, FormatStyle.MEDIUM
                    )
                )
            }

            Log.d("Date", selectedDate)
            Coroutines.io {
                appPrefs.saveValue(selectedDate, TIME_PREFS)
            }
        }
    }
}