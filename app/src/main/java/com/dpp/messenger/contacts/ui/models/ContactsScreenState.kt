package com.dpp.messenger.contacts.ui.models

import com.dpp.messenger.data.models.ContactResponse

sealed class ContactsScreenState {
    data object Loading : ContactsScreenState()
    data class Content(val contacts: List<ContactResponse>) : ContactsScreenState()
    data class Error(val loginError: String?, val nameError: String?, val passwordError: String?) : ContactsScreenState()
}