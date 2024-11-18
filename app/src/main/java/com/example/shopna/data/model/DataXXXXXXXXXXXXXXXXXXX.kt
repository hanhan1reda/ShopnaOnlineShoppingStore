package com.example.shopna.data.model

data class DataXXXXXXXXXXXXXXXXXXX(
    val address: Address,
    val cost: Double,
    val date: String,
    val discount: Int,
    val id: Int,
    val payment_method: String,
    val points: Int,
    val points_commission: Int,
    val products: List<ProductXXXXX>,
    val promo_code: String,
    val status: String,
    val total: Double,
    val vat: Double
)