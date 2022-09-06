package com.voterswik.network

import com.voterswik.model.DashboardResponseModel
import com.voterswik.model.MusicPlayerResponse
import com.voterswik.model.OtherUserProfileResponse
import com.voterswik.model.SearchResponse
import com.voterswik.ui.login.LoginResponseModel
import com.voterswik.ui.postvideo.VideoPostResponse
import com.voterswik.ui.profile.model.CommonDataResponse
import com.voterswik.ui.profile.model.VideoResponseModel
import com.voterswik.ui.profile.model.ViewProfileResponseModel
import com.voterswik.utils.ApiConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST(ApiConstant.LOGIN_URL)
    suspend fun userLogin(
        @Field("type") type: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_id") device_id: String,
        @Field("device_token") device_token: String
    ): Response<LoginResponseModel>

    @FormUrlEncoded
    @POST(ApiConstant.CREATE_USER)
    suspend fun userSignupApi(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("device_id") device_id: String,
        @Field("device_token") device_token: String
    ): Response<LoginResponseModel>

    @FormUrlEncoded
    @POST("sendotp")
    suspend fun sendOtpApi(
        @Field("username") username: String
    )
            : Response<LoginResponseModel>

    @FormUrlEncoded
    @POST("changepassword")
    suspend fun changePasswordApi(
        @Field("new_password") new_password: String,
        @Field("user_id") user_id: String
    )
            : Response<LoginResponseModel>


    @GET("viewprofile")
    suspend fun viewProfileApi(
        @Header("Authorization") authorization: String
    ): Response<ViewProfileResponseModel>

    @Multipart
    @POST("updateprofile")
    suspend fun updateProfileApi(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part image: MultipartBody.Part
    )
            : Response<LoginResponseModel>


    @GET("dashboard")
    suspend fun dashboardApi(
        @Header("Authorization") authorization: String
    ): Response<DashboardResponseModel>


    @FormUrlEncoded
    @POST("post_like")
    suspend fun postLikeApi(
        @Header("Authorization") authorization: String,
        @Field("post_id") post_id: String,
        @Field("type") type: String
    )
            : Response<CommonDataResponse>


    @FormUrlEncoded
    @POST("vote")
    suspend fun voteApi(
        @Header("Authorization") authorization: String,
        @Field("post_id") post_id: String,
        @Field("vote") vote: String
    )
            : Response<CommonDataResponse>


    @Multipart
    @POST("createvideos")
    suspend fun createVideoApi(
        @Header("Authorization") authorization: String,
        @Part video: MultipartBody.Part
    )
            : Response<VideoPostResponse>

    @GET("music_list")
    suspend fun musicListApi(
        @Header("Authorization") authorization: String
    ): Response<MusicPlayerResponse>


    @FormUrlEncoded
    @POST("search_users")
    suspend fun searchUsersListApi(
        @Header("Authorization") authorization: String,
        @Field("keyword") keyword: String
    )
            : Response<SearchResponse>


    @FormUrlEncoded
    @POST("user_likevideo_list")
    suspend fun userLikeVideoListApi(
        @Header("Authorization") authorization: String,
        @Field("type") type: String
    )
            : Response<VideoResponseModel>


    @FormUrlEncoded
    @POST("user_profile_specificdetails")
    suspend fun otherUserProfileApi(
        @Header("Authorization") authorization: String,
        @Field("user_id") user_id: String
    )
            : Response<OtherUserProfileResponse>

    @FormUrlEncoded
    @POST("user_view_post")
    suspend fun userViewPostApi(
        @Header("Authorization") authorization: String,
        @Field("post_id") post_id: String
    )
            : Response<CommonDataResponse>
}
