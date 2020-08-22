package com.utsman.hiyahiyahiya.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.utsman.hiyahiyahiya.R
import com.utsman.hiyahiyahiya.model.RowContact
import com.utsman.hiyahiyahiya.model.RowType
import com.utsman.hiyahiyahiya.utils.click
import com.utsman.hiyahiyahiya.utils.load
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.item_list_empty.view.*


class ContactAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(contact: RowContact.Contact) = itemView.run {
            img_profile.load(contact.photoUrl, isCircle = true)
            tx_name.text = contact.name
            tx_about.text = contact.about

            click {
                onContactClick?.invoke(contact)
            }
        }
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(empty: RowContact.Empty) = itemView.run {
            tx_empty_message.text = empty.text
        }
    }

    inner class ContactDiffCallback(
        private val oldList: List<RowContact>,
        private val newList: List<RowContact>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }


    private val contactList: MutableList<RowContact> = mutableListOf()
    private var onContactClick: ((RowContact.Contact) -> Unit)? = null

    fun onClick(onContactClick: ((RowContact.Contact) -> Unit)? = null) {
        this.onContactClick = onContactClick
    }

    fun addContact(contacts: List<RowContact>) = apply {
        logi("contact size -> ${contacts.size}")
        val diffCallback = ContactDiffCallback(contactList, contacts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        contactList.clear()
        contactList.addAll(contacts)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (RowType.values()[viewType]) {
            RowType.ITEM -> ContactViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
            )
            RowType.EMPTY -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_list_empty, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = contactList.size
    override fun getItemViewType(position: Int): Int = contactList[position].rowType.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = contactList[position]) {
            is RowContact.Contact -> (holder as ContactViewHolder).bind(item)
            is RowContact.Empty -> (holder as EmptyViewHolder).bind(item)
        }
    }
}