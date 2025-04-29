package com.dpp.messenger.contacts.ui.dialogs

import IncomingRequestsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpp.messenger.contacts.ui.adapters.ContactsAdapter
import com.dpp.messenger.contacts.ui.adapters.IncomingRequestsAdapter
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.databinding.FragmentIncomingRequestsBinding

class IncomingRequestsDialogFragment : DialogFragment() {

    private lateinit var contactsAdapter: ContactsAdapter

    private lateinit var binding: FragmentIncomingRequestsBinding
    private lateinit var adapter: IncomingRequestsAdapter


    private val apiService by lazy {
        RetrofitClient.create(requireContext(), requireView())
    }

    private val viewModel: IncomingRequestsViewModel by viewModels {
        IncomingRequestsViewModel.getViewModelFactory(apiService)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomingRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeRequests()
        loadRequests()

    }

    private fun setupRecyclerView() {
        adapter = IncomingRequestsAdapter(
            onAccept = { request ->
                viewModel.acceptContactRequest(request.requestId) // Changed from requestId to id or whatever your property is
                Toast.makeText(context, "Запрос принят", Toast.LENGTH_SHORT).show()
            },
            onDecline = { request ->
                viewModel.declineContactRequest(request.requestId) // Changed from requestId to id
                Toast.makeText(context, "Запрос отклонен", Toast.LENGTH_SHORT).show()
            }
        )

        binding.incomingRequestsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@IncomingRequestsDialogFragment.adapter
        }
    }


    private fun observeRequests() {
        viewModel.incomingRequests.observe(viewLifecycleOwner) { requests ->
            // Make sure requests is List<ContactRequest> and matches what the adapter expects
            adapter.updateRequests(requests)
        }
    }

    private fun loadRequests() {
        viewModel.loadIncomingRequests()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}