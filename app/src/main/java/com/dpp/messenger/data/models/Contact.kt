package com.dpp.messenger.data.models

data class Contact(val id: String, val name: String, val login: String, val avatar: String)

// Модель запроса на добавление в контакты
data class ContactRequest(
    val userId: String,
    val status: String,
    val requestId: String,
    val name: String,
    val login: String,
    val avatar: String
)

// Модель для добавления контакта
data class AddContactRequest(val userId: String)

// Модель для принятия запроса
data class AcceptContactRequest(val requestId: String)

// Модель для отклонения запроса
data class DeclineContactRequest(val requestId: String)