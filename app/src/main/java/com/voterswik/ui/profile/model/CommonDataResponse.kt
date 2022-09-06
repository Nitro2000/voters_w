package com.voterswik.ui.profile.model

import com.google.gson.annotations.SerializedName
import com.voterswik.ui.BaseModel

data class CommonDataResponse (
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : Any

): BaseModel()
