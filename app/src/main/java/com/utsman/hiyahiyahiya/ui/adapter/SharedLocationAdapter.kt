package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.data.ConstantValue
import com.utsman.hiyahiyahiya.model.row.RowSharedLocation
import com.utsman.hiyahiyahiya.utils.click
import kotlinx.android.synthetic.main.item_live_location.view.*

class SharedLocationAdapter : RecyclerView.Adapter<SharedLocationAdapter.ShareLocationViewHolder>() {

    inner class ShareLocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(rowSharedLocation: RowSharedLocation) = itemView.run {
            img_icon_item_location.setImageResource(rowSharedLocation.icon)
            tx_title_item_location.text = rowSharedLocation.title

            click {
                onItemClick?.invoke(rowSharedLocation)
            }
        }
    }

    private val list = ConstantValue.itemRowSharedLocation
    private var onItemClick: ((RowSharedLocation) -> Unit)? = null
    fun onItemClick(onItemClick: ((RowSharedLocation) -> Unit)? = null) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareLocationViewHolder {
        return ShareLocationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_live_location, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ShareLocationViewHolder, position: Int) {
        holder.bind(list[position])
    }
}