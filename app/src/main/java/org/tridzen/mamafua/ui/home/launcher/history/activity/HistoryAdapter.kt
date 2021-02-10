package org.tridzen.mamafua.ui.home.launcher.history.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import org.tridzen.mamafua.data.local.entities.Cart
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.databinding.RowChildrenBinding
import org.tridzen.mamafua.databinding.RowParentBinding
import org.tridzen.mamafua.utils.base.ExpandableAdapter
import java.util.*

class HistoryAdapter(parents: List<Order>) :
    ExpandableAdapter<Cart, Order, HistoryAdapter.PViewHolder, HistoryAdapter.CViewHolder>(
        parents as ArrayList<Order>, ExpandingDirection.VERTICAL
    ) {

    override fun onCreateParentViewHolder(parent: ViewGroup, viewType: Int): PViewHolder {
        val binding = RowParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PViewHolder(
            binding
        )
    }

    override fun onCreateChildViewHolder(child: ViewGroup, viewType: Int): CViewHolder {
        val binding = RowChildrenBinding.inflate(LayoutInflater.from(child.context), child, false)
        return CViewHolder(
            binding
        )
    }

    override fun onBindParentViewHolder(
        parentViewHolder: PViewHolder,
        expandableType: Order,
        position: Int
    ) {
        parentViewHolder.bind(expandableType)
    }

    override fun onBindChildViewHolder(
        childViewHolder: CViewHolder,
        expandedType: Cart,
        expandableType: Order,
        position: Int
    ) {
        childViewHolder.bind(expandedType)
    }


    override fun onExpandedClick(
        expandableViewHolder: PViewHolder,
        expandedViewHolder: CViewHolder,
        expandedType: Cart,
        expandableType: Order
    ) {
    }

    override fun onExpandableClick(
        expandableViewHolder: PViewHolder,
        expandableType: Order
    ) {

    }

    class PViewHolder(private val v: RowParentBinding) :
        ExpandableAdapter.ExpandableViewHolder(v.root) {
        fun bind(parent: Order) {
            v.tvTitle.text = parent.status
            v.tvPrice.text = "${parent.amount} Ksh"
            v.tvFulFiller.text = parent.fullfillerName
            v.tvDate.text = parent.updatedAt
        }
    }

    class CViewHolder(private val v: RowChildrenBinding) :
        ExpandableAdapter.ExpandedViewHolder(v.root) {
        fun bind(child: Cart) {
            v.tvCount.text = child.count.toString()
            v.tvName.text = child.service.name
            v.tvPrice.text = "${calculatePrice(child)} Ksh"
        }
    }

    companion object {
        fun calculatePrice(cart: Cart): Int {
            return when (cart.style) {
                "Itemised" -> cart.count * cart.service.offSitePrice
                "Package" -> cart.count * cart.service.offSitePrice
                else -> {
                    cart.count * cart.service.onSitePrice
                }
            }
        }
    }
}