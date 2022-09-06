package com.voterswik.network
import com.voterswik.model.DashboardResponseModel
import com.voterswik.model.MusicPlayerResponse
import com.voterswik.model.OtherUserProfileResponse
import com.voterswik.model.SearchResponse
import com.voterswik.ui.postvideo.VideoPostResponse
import com.voterswik.ui.login.LoginResponseModel
import com.voterswik.ui.profile.model.CommonDataResponse
import com.voterswik.ui.profile.model.VideoResponseModel
import com.voterswik.ui.profile.model.ViewProfileResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
interface MainRepository {
    suspend fun userSignupApi(
        fullname: String,
        email: String,
        mobile: String,
        password: String,
        device_id: String,
        device_token: String
    ): Response<LoginResponseModel>


    suspend fun userLogin(
        type: String,
        username: String,
        password: String,
        device_id: String,
        device_token: String
    ): Response<LoginResponseModel>


     suspend fun sendOtpApi(username : String) : Response<LoginResponseModel>

    suspend fun changePassword(
        new_password: String,
        user_id: String,
    ): Response<LoginResponseModel>

    suspend fun viewProfile(
        token: String,
    ): Response<ViewProfileResponseModel>

    suspend fun updateProfileApi(
        token: String,
        name: RequestBody,
        bio:RequestBody,
        email: RequestBody,
        gender: RequestBody,
        image: MultipartBody.Part,
    ): Response<LoginResponseModel>

    suspend fun dashboardApi(
        token: String,
    ): Response<DashboardResponseModel>

    suspend fun postLikeApi(
        token: String,
        post_id:String,
        type:String
    ): Response<CommonDataResponse>

    suspend fun voteApi(
        token: String,
        post_id:String,
       vote:String
    ): Response<CommonDataResponse>

    suspend fun createVideoApi(
        token: String,
        video: MultipartBody.Part,
    ): Response<VideoPostResponse>

    suspend fun musicListApi(
        token: String,
    ) : Response<MusicPlayerResponse>

    suspend fun searchListApi(
        token: String,
        keyword:String,

        ): Response<SearchResponse>

    suspend fun userLikeVideoListApi(
        token: String,
        type: String
    ):Response<VideoResponseModel>

    suspend fun otherUserProfileApi(
        token: String,
        user_id: String
    ):Response<OtherUserProfileResponse>

    suspend fun userViewPostApi(
        token: String,
        post_id: String
    ):Response<CommonDataResponse>
}


