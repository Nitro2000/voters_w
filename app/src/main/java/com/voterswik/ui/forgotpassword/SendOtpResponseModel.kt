package com.voterswik.ui.forgotpassword

import com.google.gson.annotations.SerializedName

data class SendOtpResponseModel(

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : Data?   = Data()
)

data class Data (

    @SerializedName("id"                ) var id              : Int?    = null,
    @SerializedName("type"              ) var type            : String? = null,
    @SerializedName("user_id"           ) var userId          : String? = null,
    @SerializedName("name"              ) var name            : String? = null,
    @SerializedName("email"             ) var email           : String? = null,
    @SerializedName("email_verified_at" ) var emailVerifiedAt : String? = null,
    @SerializedName("mobile"            ) var mobile          : String? = null,
    @SerializedName("token"             ) var token           : String? = null,
    @SerializedName("image"             ) var image           : String? = null,
    @SerializedName("status"            ) var status          : String? = null,
    @SerializedName("device_id"         ) var deviceId        : String? = null,
    @SerializedName("device_token"      ) var deviceToken     : String? = null,
    @SerializedName("otp"               ) var otp             : Int?    = null,
    @SerializedName("gender"            ) var gender          : String? = null,
    @SerializedName("bio"               ) var bio             : String? = null,
    @SerializedName("created_at"        ) var createdAt       : String? = null,
    @SerializedName("updated_at"        ) var updatedAt       : String? = null

)
