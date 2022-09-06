package com.voterswik.model

import com.google.gson.annotations.SerializedName
import com.voterswik.ui.BaseModel

data class DashboardResponseModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf()

) : BaseModel()

data class Data(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("video") var video: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("likes") var likes: Int? = null,
    @SerializedName("islike") var islike: Int? = null,
    @SerializedName("uservotecount") var uservotecount: Int? = null,
    @SerializedName("allvotescount") var allvotescount: Int? = null,
    @SerializedName("allpostscount") var allpostscount: Int? = null

) : BaseModel()
