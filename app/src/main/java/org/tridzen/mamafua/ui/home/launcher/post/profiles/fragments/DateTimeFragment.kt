package org.tridzen.mamafua.ui.home.launcher.post.profiles.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.tridzen.mamafua.R
import org.tridzen.mamafua.databinding.FragmentDatetimeBinding
import org.tridzen.mamafua.ui.home.launcher.post.OrdersViewModel
import org.tridzen.mamafua.ui.home.launcher.post.pay.OrderBuilder
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DateTimeFragment : Fragment(R.layout.fragment_datetime) {

    private val ordersViewModel by viewModels<OrdersViewModel>()

    @Inject
    lateinit var prefs: OrderBuilder

    private lateinit var binding: FragmentDatetimeBinding
    private var then: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDatetimeBinding.bind(view)
        binding.sdtPicker.addOnDateChangedListener { displayed, date ->
            Log.d("Date", date.toString())
        }
    }
}