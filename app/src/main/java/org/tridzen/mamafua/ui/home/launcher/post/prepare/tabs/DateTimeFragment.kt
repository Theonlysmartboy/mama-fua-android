package org.tridzen.mamafua.ui.home.launcher.post.prepare.tabs

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.tridzen.mamafua.R
import org.tridzen.mamafua.databinding.FragmentDatetimeBinding
import org.tridzen.mamafua.ui.home.launcher.post.OrdersViewModel
import org.tridzen.mamafua.ui.home.launcher.post.pay.OrderBuilder
import org.tridzen.mamafua.utils.showSnackBar
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DateTimeFragment : Fragment(R.layout.fragment_datetime), CalendarView.OnDateChangeListener,
    TimePicker.OnTimeChangedListener {

    private val ordersViewModel by viewModels<OrdersViewModel>()

    @Inject
    lateinit var prefs: OrderBuilder

    private lateinit var binding: FragmentDatetimeBinding
    private var then: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDatetimeBinding.bind(view)
        binding.butOk.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.container)

            if (returnCurrentViewId(binding.dpDateTime) == R.id.dpDateTime) {
                hideViews(binding.dpDateTime, binding.tpDateTime)
            } else {
                lifecycleScope.launch {
                    prefs.saveValue(then.timeInMillis.toString(), OrderBuilder.DELIVERY_DATE)
                }
                findNavController().navigate(R.id.action_dateTimeFragment_to_locationFragment)
            }
        }

        binding.butCancel.setOnClickListener {
            if (returnCurrentViewId(binding.dpDateTime) == R.id.tpDateTime) {
                hideViews(binding.tpDateTime, binding.dpDateTime)
            } else
                findNavController().navigateUp()
        }

        binding.dpDateTime.setOnDateChangeListener(this)
        binding.tpDateTime.setOnTimeChangedListener(this)
    }

    private fun hideViews(one: View, two: View) {
        if (one.isVisible) {
            two.visibility = View.VISIBLE
            one.visibility = View.GONE
        } else {
            two.visibility = View.GONE
            one.visibility = View.VISIBLE
        }
    }

    //TODO(check if method works and fix missing parameter)
    private fun returnCurrentViewId(one: View): Int {
        return when (true) {
            one.isVisible -> R.id.dpDateTime
            else -> R.id.tpDateTime
        }
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val datetime = Calendar.getInstance()
        val c = Calendar.getInstance()
        datetime[Calendar.HOUR_OF_DAY] = hourOfDay
        datetime[Calendar.MINUTE] = minute
        if (datetime.timeInMillis >= c.timeInMillis) {
            then.set(Calendar.HOUR_OF_DAY, hourOfDay)
            then.set(Calendar.MINUTE, minute)
            then.set(Calendar.SECOND, 0)
        } else {
            //it's before current'
            view?.showSnackBar("Cannot choose past time")
        }
    }

    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        then.set(Calendar.YEAR, year)
        then.set(Calendar.MONTH, month)
        then.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }
}