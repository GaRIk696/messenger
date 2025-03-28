package com.dpp.messenger.data.models.errors

data class err(val type: String, val title: String, val status: Int, val errors: Errors) {
    data class Errors(val Login: List<String>?, val Password: List<String>?, val Name: List<String>?)
}