package com.dpp.messenger.contacts.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpp.messenger.data.models.UserResponse
import com.dpp.messenger.databinding.ItemContactBinding

class UserSearchAdapter(private val onClick: (UserResponse) -> Unit) :
    RecyclerView.Adapter<UserSearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val contacts = mutableListOf<UserResponse>()
    private var filteredContacts = mutableListOf<UserResponse>()

    fun setContacts(newContacts: List<UserResponse>) {
        contacts.clear()
        contacts.addAll(newContacts)
        filteredContacts.clear()
        filteredContacts.addAll(newContacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemContactBinding.inflate(
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

            Glide.with(holder.itemView).load(contacts[position].avatar).into(userAvatar)
            add.setOnClickListener {
                onClick(contacts[position])
            }
        }
    }

    override fun getItemCount() = filteredContacts.size
}