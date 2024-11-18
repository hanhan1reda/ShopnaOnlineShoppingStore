package com.example.shopna.data.model

data class AddOrderRequest(
    val address_id: Int,
    val payment_method: Int,
    val use_points: Boolean
)