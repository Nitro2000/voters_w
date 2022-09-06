package com.voterswik.model

import com.google.gson.annotations.SerializedName
import com.voterswik.ui.BaseModel

data class MusicPlayerResponse(
    @SerializedName("status"  ) var status  : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<MusicPlayer> = arrayListOf()

): BaseModel()
data class MusicPlayer(

    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("music_name"    ) var music_name    : String? = null,
    @SerializedName("status"     ) var status    : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("music_url"       ) var music_url      : String? = null,

    ): BaseModel()