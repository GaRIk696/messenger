package com.dpp.messenger.contacts.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpp.messenger.R
import com.dpp.messenger.data.models.ContactRequest

class IncomingRequestsAdapter(
    private val onAccept: (ContactRequest) -> Unit,  // Изменено на ContactRequest
    private val onDecline: (ContactRequest) -> Unit   // Изменено на ContactRequest
) : RecyclerView.Adapter<IncomingRequestsAdapter.ViewHolder>() {

    private val requests = mutableListOf<ContactRequest>()

    fun updateRequests(newRequests: List<ContactRequest>) {
        requests.clear()
        requests.addAll(newRequests)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderName: TextView = itemView.findViewById(R.id.txtName)
        private val acceptButton: Button = itemView.findViewById(R.id.accept)
        private val declineButton: Button = itemView.findViewById(R.id.decline)
        private val contactAvatar: ImageView = itemView.findViewById(R.id.incomingRequestsAvatar)

        fun bind(request: ContactRequest) {
            senderName.text = request.name
            Glide.with(itemView.context)
                .load(request.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(contactAvatar)

            acceptButton.setOnClickListener { onAccept(request) }
            declineButton.setOnClickListener { onDecline(request) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(requests[position])  // Теперь типы совпадают
    }

    override fun getItemCount(): Int = requests.size
}