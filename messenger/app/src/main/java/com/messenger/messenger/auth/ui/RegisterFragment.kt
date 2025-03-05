package com.messenger.messenger.auth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.messenger.messenger.R
import com.messenger.messenger.data.RetrofitClient
import com.messenger.messenger.data.models.LoginResponse
import com.messenger.messenger.data.models.RegisterRequest
import com.messenger.messenger.data.models.errors.err
import com.messenger.messenger.databinding.FragmentRegisterBinding
import com.messenger.messenger.libs.TokenManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginError.isVisible = false
        binding.passwordError.isVisible = false
        binding.nameError.isVisible = false

        binding.registerButton.setOnClickListener{
            binding.loginError.isVisible = false
            binding.passwordError.isVisible = false
            binding.nameError.isVisible = false
            RetrofitClient.create(requireContext(), view).register(RegisterRequest(binding.Login.text.toString(), binding.Password.text.toString(), binding.Username.text.toString())).enqueue(
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
                            if (errorResponse.errors.Name != null) {
                                binding.nameError.text = errorResponse.errors.Name!!.first()
                                binding.nameError.isVisible = true
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    }
                })
        }
    }
}