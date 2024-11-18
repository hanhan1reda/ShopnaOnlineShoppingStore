package com.example.shopna.data.model

import com.google.gson.annotations.SerializedName


data class Banners (

    @SerializedName("id"       ) var id       : Int?    = null,
    @SerializedName("image"    ) var image    : String? = null,
    @SerializedName("category" ) var category : String? = null,
    @SerializedName("product"  ) var product  : String? = null

)