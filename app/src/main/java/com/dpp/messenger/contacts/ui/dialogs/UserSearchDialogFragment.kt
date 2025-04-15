package com.dpp.messenger.contacts.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpp.messenger.contacts.ui.adapters.UserSearchAdapter
import com.dpp.messenger.contacts.ui.view_models.UserSearchViewModel
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.databinding.DialogUserSearchBinding

class UserSearchDialogFragment : DialogFragment() {

    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }

    private val viewModel by viewModels<UserSearchViewModel> {
        UserSearchViewModel.getViewModelFactory(apiService)
    }

    private lateinit var binding: DialogUserSearchBinding
    private lateinit var adapter: UserSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogUserSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserSearchAdapter(
            onClick = { contact -> }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.searchUsers("")

        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.setContacts(users)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->

        }
    }

}