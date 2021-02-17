package org.tridzen.mamafua.ui.home.launcher.post.profiles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_profile.view.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.databinding.RowProfilesBinding
import org.tridzen.mamafua.utils.Constants.Companion.BASE_URL
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.data.Prefs
import org.tridzen.mamafua.utils.getName

class ProfilesAdapter(
    private val profiles: List<Profile>,
    private val prefs: Prefs,
) :
    RecyclerView.Adapter<ProfilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowProfilesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(profiles[position], prefs)
    }

    override fun getItemCount() = profiles.size

    class ViewHolder(itemView: RowProfilesBinding) : RecyclerView.ViewHolder(itemView.root) {

        val view = itemView

        fun bind(
            profile: Profile,
            prefs: Prefs,
        ) {
            val imageUrl = "${BASE_URL}/${profile.imageUrl}"

            Glide.with(view.root.context).load(imageUrl)
                .into(view.ivProfile)

            view.tvName.text = getName(profile)
            view.tvArea.text = profile.centerId
            view.cbAvailable.isChecked = profile.available
            view.rbRating0.rating = profile.rating

            view.clProfile.setOnClickListener {
                val dialog = BottomSheetDialog(view.root.context)
                val layout =
                    LayoutInflater.from(view.root.context)
                        .inflate(R.layout.dialog_profile, null, false)
                dialog.setContentView(layout)

                //Setting image on dialog
                Glide.with(view.root.context).load(imageUrl)
                    .into(layout.ivProfile1)

                //Setting provider name
                layout.tvSpName.text = getName(profile)

                //Setting provider location
                layout.tvCenter.text = profile.centerId

                //Setting rating on dialog
                layout.rbRating.rating = profile.rating

                //Setting names on other text
                layout.tvRate.text = "About ${getName(profile)}"

                layout.tvAbout.text = profile.status

                layout.tvStatus.text = availability(profile.available)

                //Button to hail provider
                layout.butPick.setOnClickListener {
                    dialog.dismiss()

//                    val order = Order(
//                        _id = profile._id + 1,
//                        fullfillerId = profile._id,
//                        fullfillerName = getName(profile),
//                        center = profile.centerId
//                    )

//                    onOrderSelectedListener.onOrderSelected(order)
                    val list = mutableMapOf<String, String>()
                    list[Prefs.FULFILLER_ID] = profile._id
                    list[Prefs.FULFILLER_NAME] = getName(profile)
                    list[Prefs.CENTER] = profile.centerId
                    Coroutines.io {
                        list.forEach {
                            prefs.setString(it.key, it.key)
                        }
                    }
                }

                //Button to cancel hailing
                layout.butCancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }
        }

        private fun availability(availability: Boolean): String = when (availability) {
            true -> "Available for work"
            else -> "Unavailable for work"
        }
    }
}