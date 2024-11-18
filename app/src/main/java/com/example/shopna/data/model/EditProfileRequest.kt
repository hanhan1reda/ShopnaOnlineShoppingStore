package com.example.shopna.data.model

data class EditProfileRequest(
    val email: String,
    val image: String,
    val name: String,
    val phone: String
)