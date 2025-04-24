package com.dpp.messenger.contacts.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dpp.messenger.contacts.ui.adapters.ContactsAdapter
import com.dpp.messenger.contacts.ui.dialogs.UserSearchDialogFragment
import com.dpp.messenger.contacts.ui.view_models.ContactsViewModel
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactsAdapter: ContactsAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactsAdapter = ContactsAdapter { contact ->
            Log.i("test", "контакт ${contact.name} был нажат")
        }
        binding.rvSearchResults.adapter = contactsAdapter

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsAdapter.setContacts(contacts)
        }

        binding.fabAddContact.setOnClickListener {
            UserSearchDialogFragment().show(childFragmentManager, "ConfirmationDialog")
        }

        binding.fabRequestContact.setOnClickListener{
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}