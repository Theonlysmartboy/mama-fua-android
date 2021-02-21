package org.tridzen.mamafua.ui.home.launcher.get.services.viewmanager

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_package.view.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.data.local.entities.Service
import org.tridzen.mamafua.databinding.RowBouquetBinding
import org.tridzen.mamafua.databinding.RowItemizedBinding
import org.tridzen.mamafua.ui.home.order.prepare.cart.CartViewModel
import org.tridzen.mamafua.utils.Constants.Companion.BASE_URL

class ServiceAdapter(
    private val entries: List<Service>,
    private val destination: String,
    private val washMode: String? = "Manual",
    private val cartViewModel: CartViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val item = 1
    private val bouquet = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == bouquet) {
            val binding =
                RowBouquetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BouquetViewHolder(binding)
        } else {
            val binding =
                RowItemizedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == bouquet) {
            (viewHolder as BouquetViewHolder).bind(entries[position], cartViewModel)
        } else {
            (viewHolder as ItemViewHolder).bind(
                entries[position],
                destination,
                washMode,
                cartViewModel
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (entries[position].description.isEmpty()) {
            item
        } else {
            bouquet
        }
    }

    override fun getItemCount() = entries.size

    class ItemViewHolder(val binding: RowItemizedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            service: Service,
            destination: String,
            washMode: String?,
            cartViewModel: CartViewModel
        ) {
            binding.tvName.text = service.name
            binding.tvName.isSelected = true
            cartViewModel.cart.observeForever {
                val filtered = it!!.find { cart -> cart.id == service._id }

                if (filtered != null) {
                    binding.itvCount.text = filtered.count.toString()
                } else {
                    binding.itvCount.text = "0"
                }
            }
            when (destination) {
                "Itemized" -> {
                    binding.tvPrice.text = service.offSitePrice.toString()
                }

                "Delivery" -> {
                    when (washMode) {
                        "Manual" -> binding.tvPrice.text = service.onSitePrice.toString()

                        "Machine" -> binding.tvPrice.text = service.machinePrice.toString()
                    }
                }
            }

            binding.ivPlus.setOnClickListener {
                when (destination) {
                    "Itemized" -> insertEntry(service, "Itemized", cartViewModel)

                    "Delivery" -> {
                        when (washMode) {
                            "Manual" -> insertEntry(service, "Manual", cartViewModel)

                            "Machine" -> insertEntry(service, "Machine", cartViewModel)
                        }
                    }
                }
            }

            binding.ivMinus.setOnClickListener {
                deleteEntry(service, "Itemized", cartViewModel)
            }

//            Glide.with(binding.root).load("${BASE_URL}/${service.imageUrl}")
//                .into(binding.ivItem)
            glideAway(
                "${BASE_URL}/${service.imageUrl}",
                binding.ivItem,
                binding.cdItem,
                binding.root.context
            )
        }
    }

    class BouquetViewHolder(val binding: RowBouquetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service, cartViewModel: CartViewModel) {
            Glide.with(binding.root).load("${BASE_URL}/${service.imageUrl}")
                .into(binding.ivBouquet)

            binding.tvbName.text = service.name
            binding.tvbName.isSelected = true
            binding.tvbPrice.text = service.offSitePrice.toString()

            cartViewModel.cart.observeForever {
                val filtered = it!!.find { cart -> cart.id == service._id }

                if (filtered != null) {
                    binding.itvCount.text = filtered.count.toString()
                } else {
                    binding.itvCount.text = "0"
                }
            }

            binding.ivPlus.setOnClickListener {
                insertEntry(service, "Package", cartViewModel)
            }

            binding.ivMinus.setOnClickListener {
                deleteEntry(service, "Package", cartViewModel)
            }

            binding.cdBouquet.setOnClickListener {
                val dialog = BottomSheetDialog(binding.root.context)
                val layout =
                    LayoutInflater.from(binding.root.context)
                        .inflate(R.layout.dialog_package, null, false)

                layout.butTwo.text = "Close"
                layout.butOne.visibility = View.INVISIBLE

                layout.butTwo.setOnClickListener {
                    dialog.dismiss()
                }

                layout.tvTitle.text = service.name
                layout.tvDesc.text = let { service.description }
                dialog.setContentView(layout)
                dialog.show()
            }
        }
    }


    companion object {
        fun insertEntry(service: Service, style: String, cartViewModel: CartViewModel) =
            cartViewModel.insertEntry(
                Cart(
                    id = service._id,
                    service = service,
                    style = style,
                )
            )

        fun deleteEntry(service: Service, style: String, cartViewModel: CartViewModel) =
            cartViewModel.deleteEntry(
                Cart(
                    id = service._id,
                    service = service,
                    style = style,
                )
            )

        fun glideAway(image: String, imageView: ImageView, cardView: CardView, context: Context) =
            Glide.with(context)
                .asBitmap()
                .load(image)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
                            val p: Palette = Palette.from(resource).generate()
                            // Use generated instance
                            val color = p.getMutedColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.colorBlueMidnight
                                )
                            );

                            cardView.setCardBackgroundColor(color)
//                        view.clPayment.setBackgroundColor(color)

                        }
                        return false
                    }
                })
                .into(imageView)
    }
}