package com.messenger.messenger.contacts.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.messenger.messenger.contacts.ui.view_models.UserSearchViewModel
import com.messenger.messenger.data.RetrofitClient
import com.messenger.messenger.databinding.DialogUserSearchBinding

class UserSearchDialogFragment : DialogFragment() {

    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }

    private val viewModel by viewModels<UserSearchViewModel> {
        UserSearchViewModel.getViewModelFactory(apiService)
    }

    private lateinit var binding: DialogUserSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogUserSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
}