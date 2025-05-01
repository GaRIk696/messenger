package com.dpp.messenger.auth.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dpp.messenger.R
import com.dpp.messenger.auth.ui.view_models.RegisterViewModel
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.data.models.RegisterRequest
import com.dpp.messenger.databinding.FragmentRegisterBinding
import com.dpp.messenger.libs.TokenManager

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }

    private val viewModel by viewModels<RegisterViewModel> {
        RegisterViewModel.getViewModelFactory(apiService)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupClickListeners()
        hideAllErrors()
    }

    private fun setupObservers() {
        viewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            response?.token?.let { token ->
                TokenManager.saveToken(requireContext(), token)
                findNavController().navigate(R.id.action_authFragment_to_chatsFragment)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
            }
        }

        viewModel.validationErrors.observe(viewLifecycleOwner) { (loginError, nameError, passwordError) ->
            binding.loginError.apply {
                text = loginError
                isVisible = !loginError.isNullOrEmpty()
            }
            binding.nameError.apply {
                text = nameError
                isVisible = !nameError.isNullOrEmpty()
            }
            binding.passwordError.apply {
                text = passwordError
                isVisible = !passwordError.isNullOrEmpty()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.registerButton.isEnabled = !isLoading
        }
    }

    private fun setupClickListeners() {
        binding.registerButton.setOnClickListener {
            hideAllErrors()
            val request = RegisterRequest(
                login = binding.Username.text.toString(),
                name = binding.Login.text.toString(),
                password = binding.Password.text.toString()
            )
            viewModel.registerUser(request)
        }
    }

    private fun hideAllErrors() {
        binding.loginError.isVisible = false
        binding.nameError.isVisible = false
        binding.passwordError.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}