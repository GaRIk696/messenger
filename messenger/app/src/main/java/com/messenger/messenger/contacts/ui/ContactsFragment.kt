package com.messenger.messenger.contacts.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.messenger.messenger.contacts.ui.adapters.ContactsAdapter
import com.messenger.messenger.contacts.ui.view_models.ConfirmationDialogFragment
import com.messenger.messenger.contacts.ui.view_models.ContactsViewModel
import com.messenger.messenger.data.RetrofitClient
import com.messenger.messenger.data.models.ContactResponse
import com.messenger.messenger.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }
    private val viewModel by viewModels<ContactsViewModel> {
        ContactsViewModel.getViewModelFactory(apiService)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var contactsAdapter: ContactsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
        }
        viewModel.updateContacts(emptyList())

        binding.fabAddContact.setOnClickListener {
            ConfirmationDialogFragment().show(childFragmentManager, "ConfirmationDialog")
        }

        contactsAdapter = ContactsAdapter { contact -> Log.i("test", "контакт ${contact.name} был нажат")
        }

        binding.rvSearchResults.adapter = contactsAdapter

        val contacts = listOf<ContactResponse>()

        contactsAdapter.setContacts(contacts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}