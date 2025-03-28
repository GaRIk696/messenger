package com.dpp.messenger.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dpp.messenger.R
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.data.models.LoginRequest
import com.dpp.messenger.data.models.LoginResponse
import com.dpp.messenger.data.models.errors.err
import com.dpp.messenger.databinding.FragmentLoginBinding
import com.dpp.messenger.libs.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginError.isVisible = false
        binding.passwordError.isVisible = false
        binding.loginOrPasswordError.isVisible = false

        binding.AuthButton.setOnClickListener {
            binding.loginError.isVisible = false
            binding.passwordError.isVisible = false
            binding.loginOrPasswordError.isVisible = false
            RetrofitClient.create(requireContext(), view).login(LoginRequest(binding.Login.text.toString(), binding.Password.text.toString())).enqueue(
                object :
                    Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.token?.let { token ->
                                TokenManager.saveToken(requireContext(), token)
                                findNavController().navigate(R.id.action_authFragment_to_chatsFragment)
                            }
                        } else if (response.code() == 422) {
                            val gson = Gson()
                            val type = object : TypeToken<err>() {}.type
                            var errorResponse: err? = gson.fromJson(response.errorBody()!!.charStream(), type)
                            if (errorResponse == null)
                                return

                            if (errorResponse.errors.Login != null) {
                                binding.loginError.text = errorResponse.errors.Login!!.first()
                                binding.loginError.isVisible = true
                            }
                            if (errorResponse.errors.Password != null) {
                                binding.passwordError.text = errorResponse.errors.Password!!.first()
                                binding.passwordError.isVisible = true
                            }
                        } else if (response.code() == 400) {
                            binding.loginOrPasswordError.text = response.errorBody()?.charStream()?.readText()
                            binding.loginOrPasswordError.isVisible = true
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    }
                })
        }
    }
}