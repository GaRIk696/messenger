package com.dpp.messenger.contacts.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpp.messenger.data.models.ContactResponse
import com.dpp.messenger.databinding.ItemUserBinding

class ContactsAdapter(private val onClick: (ContactResponse) -> Unit) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>(), Filterable {

    inner class ViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val contacts = mutableListOf<ContactResponse>()
    private var filteredContacts = mutableListOf<ContactResponse>()

    fun setContacts(newContacts: List<ContactResponse>) {
        contacts.clear()
        contacts.addAll(newContacts)
        filteredContacts.clear()
        filteredContacts.addAll(newContacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder.binding) {
            txtName.text = filteredContacts[position].name
            txtLogin.text = filteredContacts[position].login
            Glide.with(holder.itemView).load(filteredContacts[position].avatar).into(userAvatar)

            root.setOnClickListener {
                onClick(filteredContacts[position])
            }
        }
    }

    override fun getItemCount() = filteredContacts.size
    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val filtered: MutableList<ContactResponse> = mutableListOf()

            if (constraint.isNullOrEmpty()) {
                filtered.addAll(contacts)
            } else {

                for (contact in contacts) {
                    if (contact.name.contains(constraint, true)) {
                        filtered.add(contact)
                    }
                }
            }
            results.values = filtered
            results.count = filtered.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredContacts = results?.values as? MutableList<ContactResponse> ?: mutableListOf()
            notifyDataSetChanged()
        }
    }
}