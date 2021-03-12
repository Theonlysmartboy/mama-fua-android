package org.tridzen.mamafua.ui.home.order.prepare.cart

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.first
import org.tridzen.mamafua.data.local.entities.Params
import org.tridzen.mamafua.data.local.entities.Profile
import org.tridzen.mamafua.data.remote.AppPreferences
import org.tridzen.mamafua.databinding.RowLBinding
import org.tridzen.mamafua.databinding.RowPBinding
import org.tridzen.mamafua.databinding.RowTBinding
import org.tridzen.mamafua.ui.home.order.profiles.FinalActivity
import org.tridzen.mamafua.utils.Constants
import org.tridzen.mamafua.utils.coroutines.Coroutines
import org.tridzen.mamafua.utils.getLatLng
import org.tridzen.mamafua.utils.getName
import java.util.*

class ParamsAdapter(
) :
    ListAdapter<Params, RecyclerView.ViewHolder>(ParamsDiffCallBack()) {

    private val time = 1
    private val mama = 2
    private val location = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            mama -> {
                val binding =
                    RowPBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProfileViewHolder(binding)
            }

            time -> {
                val binding =
                    RowTBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TimeViewHolder(binding)
            }

            else -> {
                val binding =
                    RowLBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LocationViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == time -> {
                (viewHolder as TimeViewHolder).bind()
            }
            getItemViewType(position) == mama -> {
                (viewHolder as ProfileViewHolder).bind(
                    getItem(position).profile
                )
            }
            getItemViewType(position) == location -> {
                (viewHolder as LocationViewHolder).bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item.id) {
            "Time" -> 1
            "Profile" -> 2
            else -> 3
        }
    }

    class TimeViewHolder(binding: RowTBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
        fun bind() {
            Coroutines.main {
                val time =
                    AppPreferences(view.root.context).getValue(AppPreferences.TIME_PREFS).first()
                view.tvDate.text = time
            }

            view.cdTime.setOnClickListener {

                val activity = FinalActivity()

                view.root.context.startActivity(
                    Intent(
                        view.root.context,
                        FinalActivity::class.java
                    )
                )
            }
        }
    }

    class ProfileViewHolder(binding: RowPBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
        fun bind(profile: Profile?) {
            view.tvName.text = profile?.let { getName(it) }
            view.tvArea.text = profile?.centerId
            Glide.with(view.root).load("${Constants.BASE_URL}/${profile?.imageUrl}")
                .into(view.ivProfile)

            view.clProfile.setOnClickListener {
                view.root.context.startActivity(
                    Intent(
                        view.root.context,
                        FinalActivity::class.java
                    )
                )
            }
        }
    }

    class LocationViewHolder(binding: RowLBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
        fun bind() {
            val gcd = Geocoder(view.root.context, Locale.getDefault())
            var latLng: CartFragment.LatLng = CartFragment.LatLng()

            Coroutines.main {
                latLng = getLatLng(view.root.context)
            }.invokeOnCompletion {
                val addresses: List<Address> =
                    gcd.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses.isNotEmpty()) {
                    view.tvLocation.text = addresses[0].featureName + "," + addresses[0].adminArea
                }
            }

            view.cdLocation.setOnClickListener {
                view.root.context.startActivity(
                    Intent(
                        view.root.context,
                        FinalActivity::class.java
                    )
                )
            }
        }
    }

    class ParamsDiffCallBack : DiffUtil.ItemCallback<Params>() {
        override fun areItemsTheSame(oldItem: Params, newItem: Params): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Params, newItem: Params): Boolean {
            return oldItem == newItem
        }
    }
}
