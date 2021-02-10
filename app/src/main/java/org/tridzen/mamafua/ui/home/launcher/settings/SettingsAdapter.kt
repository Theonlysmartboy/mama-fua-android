package org.tridzen.mamafua.ui.home.launcher.settings

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_package.view.*
import kotlinx.android.synthetic.main.row_dialog.view.*
import kotlinx.android.synthetic.main.row_switch.view.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.ui.auth.AuthActivity
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
            DialogViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == switch) {
            (holder as SwitchViewHolder).bind(list[position])
        } else {
            onLogoutHandler?.let { (holder as DialogViewHolder).bind(list[position], it) }
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
            Coroutines.main {
                ThemeHelper.getTheme(view.context).asLiveData().observeForever {
                    view.tvsSetting.text = it
                    view.scsCheck.isChecked = it == "Dark"
                }
            }

            view.scsCheck.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    ThemeHelper.applyTheme(DARK_MODE)
                    ThemeHelper.getTheme(view.context)
                } else {
                    ThemeHelper.applyTheme(LIGHT_MODE)
                    ThemeHelper.getTheme(view.context)
                }
            }
        }
    }

    class DialogViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(settings: Settings, logoutHandler: OnLogoutHandler) {
            view.ivdIcon.setImageResource(settings.image)
            view.tvdTitle.text = settings.title
//            view.tvdSetting.text = settings.options?.get(0)

            view.clDialog.setOnClickListener {
                val dialog = BottomSheetDialog(view.context)
                val layout =
                    LayoutInflater.from(view.context).inflate(R.layout.dialog_package, null, false)

                layout.tvTitle.text = settings.title
                layout.tvDesc.text = settings.description
                layout.butOne.text = settings.options?.get(0) ?: ""
                layout.butTwo.text = settings.options?.get(1) ?: ""


                layout.butOne.setOnClickListener {
                    when (this.adapterPosition) {
                        1 -> {
                            logoutHandler.onLogout(true)
                            dialog.dismiss()
                            view.context.apply {
                                startActivity(Intent(this, AuthActivity::class.java))
                            }
                        }
                        else -> dialog.dismiss()
                    }
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