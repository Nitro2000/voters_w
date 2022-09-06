package com.voterswik.ui.postvideo

import com.google.gson.annotations.SerializedName


data class VideoPostResponse (

    @SerializedName("status"  ) var status  : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Boolean? = null

)