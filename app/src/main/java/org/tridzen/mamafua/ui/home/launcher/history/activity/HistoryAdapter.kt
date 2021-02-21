package org.tridzen.mamafua.ui.home.launcher.history.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import org.tridzen.mamafua.data.local.entities.Order
import org.tridzen.mamafua.data.local.entities.CartX
import org.tridzen.mamafua.databinding.RowChildrenBinding
import org.tridzen.mamafua.databinding.RowParentBinding
import org.tridzen.mamafua.utils.base.ExpandableAdapter
import java.util.*

class HistoryAdapter(parents: List<Order>) :
    ExpandableAdapter<CartX, Order, HistoryAdapter.PViewHolder, HistoryAdapter.CViewHolder>(
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
        expandedType: CartX,
        expandableType: Order,
        position: Int
    ) {
        childViewHolder.bind(expandedType)
    }


    override fun onExpandedClick(
        expandableViewHolder: PViewHolder,
        expandedViewHolder: CViewHolder,
        expandedType: CartX,
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
            v.tvFulFiller.text = parent.profileName
            v.tvDate.text = parent.updatedAt
        }
    }

    class CViewHolder(private val v: RowChildrenBinding) :
        ExpandableAdapter.ExpandedViewHolder(v.root) {
        fun bind(child: CartX) {
            v.tvCount.text = child.count.toString()
            v.tvName.text = child.name
            v.tvPrice.text = "${calculatePrice(child)} Ksh"
        }
    }

    companion object {
        fun calculatePrice(cart: CartX): Int {
            return cart.count * cart.price
        }
    }
}