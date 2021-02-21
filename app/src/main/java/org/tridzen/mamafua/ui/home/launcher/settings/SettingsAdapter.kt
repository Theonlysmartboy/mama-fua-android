package org.tridzen.mamafua.ui.home.launcher.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_package.view.*
import kotlinx.android.synthetic.main.row_dialog.view.*
import kotlinx.android.synthetic.main.row_switch.view.*
import kotlinx.coroutines.flow.first
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.data.remote.AppPreferences.Companion.SHOW_REFERRALS
import org.tridzen.mamafua.ui.home.interfaces.OnLogoutHandler
import org.tridzen.mamafua.utils.ThemeHelper
import org.tridzen.mamafua.utils.ThemeHelper.DARK_MODE
import org.tridzen.mamafua.utils.ThemeHelper.LIGHT_MODE
import org.tridzen.mamafua.utils.coroutines.Coroutines

class SettingsAdapter(private val list: List<Settings>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val switch = 1
    private val dialog = 2
    var onLogoutHandler: OnLogoutHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == switch) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.row_switch, parent, false)
            SwitchViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.row_dialog, parent, false)
            SwitchViewHolder.DialogViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == switch) {
            (holder as SwitchViewHolder).bind(list[position])
        } else {
            onLogoutHandler?.let {
                (holder as SwitchViewHolder.DialogViewHolder).bind(
                    list[position],
                    it
                )
            }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        return if (list[position].options == null) {
            switch
        } else {
            dialog
        }
    }

    class SwitchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(settings: Settings) {
            view.ivsIcon.setImageResource(settings.image)
            view.tvsTitle.text = settings.title

            when (layoutPosition) {
                0 -> {
                    Coroutines.main {
                        ThemeHelper.getTheme(view.context).asLiveData().observeForever {
                            view.tvsSetting.text = it
                            view.scsCheck.isChecked = it == "Dark"
                        }
                    }

                    view.scsCheck.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            ThemeHelper.applyTheme(DARK_MODE, view.context)
                        } else {
                            ThemeHelper.applyTheme(LIGHT_MODE, view.context)
                        }
                    }
                }

                1 -> {
                    Coroutines.main {
                        val show = AppPreferences(view.context).getValue(SHOW_REFERRALS).first()
                        view.tvsSetting.text =
                            if (show == true) "Always show" else "Don't show"
                        view.scsCheck.isChecked = show == true
                    }
                    view.scsCheck.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            view.tvsSetting.text = settings.settings!![0]
                            Coroutines.main {
                                AppPreferences(view.context).saveValue(true, SHOW_REFERRALS)
                            }
                        } else {
                            view.tvsSetting.text = settings.settings!![1]
                            Coroutines.main {
                                AppPreferences(view.context).saveValue(false, SHOW_REFERRALS)
                            }
                        }
                    }
                }
            }
        }

        class DialogViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

            fun bind(settings: Settings, logoutHandler: OnLogoutHandler) {
                view.ivdIcon.setImageResource(settings.image)
                view.tvdTitle.text = settings.title

                Log.d("Position", this.adapterPosition.toString())

                view.clDialog.setOnClickListener {
                    val dialog = BottomSheetDialog(view.context)
                    val layout =
                        LayoutInflater.from(view.context)
                            .inflate(R.layout.dialog_package, null, false)

                    layout.tvTitle.text = settings.title
                    layout.tvDesc.text = settings.description
                    layout.butOne.text = settings.options?.get(0) ?: ""
                    layout.butTwo.text = settings.options?.get(1) ?: ""

                    when (this.adapterPosition) {
                        4 -> {
                            layout.butOne.setOnClickListener {
                                logoutHandler.onLogout(true)
                                dialog.dismiss()
                            }
                        }
                        else -> dialog.dismiss()
                    }

                    layout.butTwo.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.setContentView(layout)
                    dialog.show()
                }
            }
        }
    }
}