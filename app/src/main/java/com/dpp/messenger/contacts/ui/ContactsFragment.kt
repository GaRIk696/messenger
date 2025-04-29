package com.dpp.messenger.contacts.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpp.messenger.R
import com.dpp.messenger.contacts.ui.adapters.ContactsAdapter
import com.dpp.messenger.contacts.ui.view_models.ContactsViewModel
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactsAdapter: ContactsAdapter

    private val apiService by lazy {
        RetrofitClient.create(requireContext(), requireView())
    }

    val viewModel: ContactsViewModel by viewModels {
        ContactsViewModel.getViewModelFactory(apiService)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()

        viewModel.loadContacts()

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsAdapter.setContacts(contacts)
        }

        binding.fabRequestContact.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_incomingRequestsDialogFragment)
        }

        binding.fabAddContact.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_userSearchDialogFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        contactsAdapter = ContactsAdapter { contact ->
            Log.i("ContactsFragment", "Contact ${contact.name} clicked")
        }
        binding.rvSearchResults.adapter = contactsAdapter
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { contactsAdapter.filter.filter(it) }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}