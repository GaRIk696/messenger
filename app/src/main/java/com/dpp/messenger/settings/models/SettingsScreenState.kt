package com.dpp.messenger.settings.models

import com.dpp.messenger.data.models.UserResponse

sealed class SettingsScreenState {
    data object Loading : SettingsScreenState()
    data object NavAuth : SettingsScreenState()
    data class Error(val errorMessage: String): SettingsScreenState()
    data class Content(val user: UserResponse?, val avatar: String?) : SettingsScreenState()
}