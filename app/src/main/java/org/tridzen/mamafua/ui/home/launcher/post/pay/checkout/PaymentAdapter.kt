package org.tridzen.mamafua.ui.home.launcher.post.pay.checkout

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.row_payment.view.*
import org.tridzen.mamafua.R
import org.tridzen.mamafua.data.local.entities.Payment

class PaymentAdapter(
    private val list: List<Payment>,
    private val listener: OnPaymentListener
) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_payment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount() = list.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(payment: Payment, listener: OnPaymentListener) {
            glideAway(payment)
            view.tvName.text = payment.name
            view.tvNumber.text = payment.number
            view.cdPayment.setOnClickListener {
                listener.editPayment(payment)
            }

            view.cdPayment.setOnLongClickListener {
                listener.deletePayment(payment)
                true
            }

            view.lavPayment.setAnimation(if (payment.orders.isEmpty()) R.raw.pending else R.raw.verified)
        }

        private fun glideAway(payment: Payment) = Glide.with(view.context)
            .asBitmap()
            .load(payment.icon)
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
                        val color = p.getDarkVibrantColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.colorBlueMidnight
                            )
                        );

                        view.cdPayment.setCardBackgroundColor(color)
                        view.clPayment.setBackgroundColor(color)

                    }
                    return false
                }
            })
            .into(view.ivIcon)
    }
}