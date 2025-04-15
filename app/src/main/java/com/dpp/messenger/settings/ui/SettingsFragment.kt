package com.dpp.messenger.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dpp.messenger.R
import com.dpp.messenger.data.RetrofitClient
import com.dpp.messenger.databinding.FragmentSettingsBinding
import com.dpp.messenger.libs.ThemeManager
import com.dpp.messenger.libs.TokenManager
import com.dpp.messenger.settings.models.SettingsScreenState
import com.dpp.messenger.settings.viewmodels.SettingsViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.getViewModelFactory(apiService)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { url ->
                if (url != null) {
                    val type = requireContext().contentResolver.getType(url)
                        ?: return@registerForActivityResult

                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(type)
                        ?: return@registerForActivityResult

                    val requestBody =
                        requireContext().contentResolver.openInputStream(url).use {
                            it?.readBytes()?.toRequestBody(type.toMediaTypeOrNull())
                        } ?: return@registerForActivityResult

                    val filePart = MultipartBody.Part.createFormData(
                        "file",
                        url.lastPathSegment + "." + extension,
                        requestBody
                    )

                    viewModel.uploadUserAvatar(filePart)
                }
            }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

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
            viewModel.logout()
        }

        binding.userAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun render(state: SettingsScreenState) {
        when (state) {
            is SettingsScreenState.Loading -> showLoading()
            is SettingsScreenState.Content -> showContent(state)
            is SettingsScreenState.Error -> showError(state)
            is SettingsScreenState.NavAuth -> navAuth()
        }
    }

    private fun hideAll() {
        binding.progressBar.isVisible = false
    }

    private fun showContent(state: SettingsScreenState.Content) {
        hideAll()
        if (state.user != null) {
            binding.user.text = state.user.name

            if (state.user.avatar != null) {
                Glide.with(this)
                    .load(state.user.avatar)
                    .placeholder(
                        R.drawable.ic_launcher_background
                    )
                    .fitCenter()
                    .transform(RoundedCorners(10))
                    .into(binding.userAvatar)
            }
        } else if (state.avatar != null){
            Glide.with(this)
                .load(state.avatar)
                .placeholder(
                    R.drawable.ic_launcher_background
                )
                .fitCenter()
                .transform(RoundedCorners(10))
                .into(binding.userAvatar)
        }
    }

    private fun showLoading() {
        hideAll()
        binding.progressBar.isVisible = true
    }

    private fun showError(state: SettingsScreenState.Error) {
        hideAll()
        binding.user.text = state.errorMessage
    }

    private fun navAuth() {
        TokenManager.clearToken(requireContext())
        findNavController().navigate(com.dpp.messenger.R.id.action_settingsFragment_to_authFragment)
    }

    private fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
