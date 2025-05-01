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
import com.dpp.messenger.auth.ui.view_models.LoginViewModel
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.data.models.LoginRequest
import com.dpp.messenger.databinding.FragmentLoginBinding
import com.dpp.messenger.libs.TokenManager

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }

    private val viewModel by viewModels<LoginViewModel> {
        LoginViewModel.getViewModelFactory(apiService)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        binding.AuthButton.setOnClickListener {
            val login = binding.Login.text.toString()
            val password = binding.Password.text.toString()
            viewModel.login(LoginRequest(login, password))
        }
    }

    private fun setupObservers() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            response?.token?.let { token ->
                TokenManager.saveToken(requireContext(), token)
                findNavController().navigate(R.id.action_authFragment_to_chatsFragment)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            binding.loginOrPasswordError.text = error
            binding.loginOrPasswordError.isVisible = error != null
        }

        viewModel.validationErrors.observe(viewLifecycleOwner) { (loginError, passwordError) ->
            binding.loginError.text = loginError
            binding.loginError.isVisible = !loginError.isNullOrEmpty()
            binding.passwordError.text = passwordError
            binding.passwordError.isVisible = !passwordError.isNullOrEmpty()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.AuthButton.isEnabled = !isLoading
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}