package com.voterswik.ui.profile.model

import com.google.gson.annotations.SerializedName
import com.voterswik.ui.BaseModel

data class VideoResponseModel (
    @SerializedName("status"  ) var status  : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<VideoData> = arrayListOf()

): BaseModel()

data class VideoData (

    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("user_id"    ) var userId    : String? = null,
    @SerializedName("video"      ) var video     : String? = null,
    @SerializedName("status"     ) var status    : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("likes"      ) var likes     : Int?    = null,
    @SerializedName("dislikes"   ) var dislikes  : Int?    = null

): BaseModel()

