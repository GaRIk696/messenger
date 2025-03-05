package com.messenger.messenger.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.messenger.messenger.data.RetrofitClient
import com.messenger.messenger.data.models.UserResponse
import com.messenger.messenger.databinding.FragmentSettingsBinding
import com.messenger.messenger.libs.ThemeManager
import com.messenger.messenger.libs.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitClient.create(requireContext(), view).getUser().enqueue(
            object :
                Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.name.let { name ->
                            binding.user.text = name
                        }
                    }
                }

                override fun onFailure(p0: Call<UserResponse?>, p1: Throwable) {
                }
            })

        binding.themeSwitcher.isChecked = ThemeManager.getTheme(requireContext())
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setDarkTheme()
            } else {
                setLightTheme()
            }
            ThemeManager.saveTheme(requireContext(), isChecked)
        }

        binding.logout.setOnClickListener {
            RetrofitClient.create(requireContext(), view).logout().enqueue(
                object :
                    Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            TokenManager.clearToken(requireContext())
                            findNavController().navigate(com.messenger.messenger.R.id.action_settingsFragment_to_authFragment)
                        } else {
                        }
                    }

                    override fun onFailure(p0: Call<Void?>, p1: Throwable) {
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}