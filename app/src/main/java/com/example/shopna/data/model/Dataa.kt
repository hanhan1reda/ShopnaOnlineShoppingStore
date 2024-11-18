package com.example.shopna.data.model

import com.google.gson.annotations.SerializedName


data class Dataa (

    @SerializedName("banners"  ) var banners  : ArrayList<Banners>  = arrayListOf(),
    @SerializedName("products" ) var products : ArrayList<Products> = arrayListOf(),
    @SerializedName("ad"       ) var ad       : String?             = null

)