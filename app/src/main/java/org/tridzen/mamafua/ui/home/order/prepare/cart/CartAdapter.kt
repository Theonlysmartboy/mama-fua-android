package org.tridzen.mamafua.ui.home.order.prepare.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.databinding.RowBBinding
import org.tridzen.mamafua.databinding.RowIBinding
import org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager.ServiceAdapter
import org.tridzen.mamafua.utils.Constants.Companion.BASE_URL

class CartAdapter(
) :
    ListAdapter<Cart, RecyclerView.ViewHolder>(CartDiffCallBack()) {

    private val bouquet = 1
    private val single = 2

    var cartViewModel: CartViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: Any?
        return when (viewType) {
            bouquet -> {
                binding =
                    RowBBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BouquetViewHolder(binding, cartViewModel!!)
            }

            else -> {
                binding =
                    RowIBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SingleViewHolder(binding, cartViewModel!!)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == bouquet -> {
                (viewHolder as BouquetViewHolder).bind(getItem(position))
            }
            getItemViewType(position) == single -> {
                (viewHolder as SingleViewHolder).bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item.service.description) {
            "" -> single
            else -> bouquet
        }
    }

    class BouquetViewHolder(val view: RowBBinding, private val cartViewModel: CartViewModel) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: Cart) {
            view.btvName.text = item.service.name
            view.btvPrice.text = item.service.offSitePrice.toString()
            view.tvCount.text = item.count.toString()

            view.butAdd.setOnClickListener {
                insertEntry(item, cartViewModel)
            }

            view.butMinus.setOnClickListener {
                deleteEntry(item, cartViewModel)
            }

            Glide.with(view.root).load("$BASE_URL/${item.service.imageUrl}")
                .into(view.bivItem)
        }
    }

    class SingleViewHolder(val view: RowIBinding, private val cartViewModel: CartViewModel) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(singles: Cart) {
            view.itvName.text = singles.service.name
            view.itvCount.text = singles.count.toString()

            view.butAdd.setOnClickListener {
                insertEntry(singles, cartViewModel)
            }

            view.butMinus.setOnClickListener {
                deleteEntry(singles, cartViewModel)
            }

            when (singles.style) {
                "Itemized" -> view.itvPrice.text = singles.service.offSitePrice.toString()

                "Manual" -> view.itvPrice.text = singles.service.onSitePrice.toString()

                "Machine" -> view.itvPrice.text = singles.service.machinePrice.toString()
            }
//            Glide.with(view.root).load("${BASE_URL}/${singles.service.imageUrl}")
//                .into(view.iivItem)

            ServiceAdapter.glideAway(
                "${BASE_URL}/${singles.service.imageUrl}",
                view.iivItem,
                view.icdItem,
                view.root.context
            )
        }
    }

    companion object {
        fun insertEntry(cart: Cart, cartViewModel: CartViewModel) =
            cartViewModel.insertEntry(cart)

        fun deleteEntry(cart: Cart, cartViewModel: CartViewModel) =
            cartViewModel.deleteEntry(cart)
    }

    class CartDiffCallBack : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }
}