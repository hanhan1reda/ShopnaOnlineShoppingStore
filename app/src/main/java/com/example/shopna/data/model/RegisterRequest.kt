package com.example.shopna.data.model

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String,
    val phone: String
)