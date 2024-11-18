package com.example.shopna.data.model

import com.google.gson.annotations.SerializedName

data class Home (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var dataa    : Dataa?    = Dataa()

)