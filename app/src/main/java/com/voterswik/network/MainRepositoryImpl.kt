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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val apiService: ApiService) : MainRepository {


    override suspend fun userLogin(
        type: String,
        username: String,
        password: String,
        device_id: String,
        device_token: String
    ): Response<LoginResponseModel> =
        apiService.userLogin(type, username, password, device_id, device_token)

    override suspend fun userSignupApi(
        fullname: String,
        email: String,
        mobile: String,
        password: String,
        device_id: String,
        device_token: String
    ): Response<LoginResponseModel> =
        apiService.userSignupApi(fullname, email, mobile, password, device_id, device_token)

    override suspend fun sendOtpApi(
        username: String,
    ): Response<LoginResponseModel> = apiService.sendOtpApi(username)

    override suspend fun changePassword(
        new_password: String,
        user_id: String
    ): Response<LoginResponseModel> = apiService.changePasswordApi(new_password, user_id)

    override suspend fun viewProfile(token: String): Response<ViewProfileResponseModel> =
        apiService.viewProfileApi(token)

    override suspend fun updateProfileApi(
        token: String,
        name: RequestBody,
        bio: RequestBody,
        email: RequestBody,
        gender: RequestBody,
        image: MultipartBody.Part
    ): Response<LoginResponseModel> =
        apiService.updateProfileApi(token, name, bio, email, gender, image)

    override suspend fun dashboardApi(token: String): Response<DashboardResponseModel> =
        apiService.dashboardApi(token)

    override suspend fun postLikeApi(
        token: String,
        post_id: String,
        type: String
    ): Response<CommonDataResponse> = apiService.postLikeApi(token, post_id, type)

    override suspend fun voteApi(
        token: String,
        post_id: String,
        vote: String
    ): Response<CommonDataResponse> = apiService.voteApi(token, post_id, vote)

    override suspend fun createVideoApi(
        token: String,
        video: MultipartBody.Part
    ): Response<VideoPostResponse> = apiService.createVideoApi(token, video)

    override suspend fun musicListApi(token: String): Response<MusicPlayerResponse> =
        apiService.musicListApi(token)

    override suspend fun searchListApi(
        token: String,
        keyword: String,
    ): Response<SearchResponse> = apiService.searchUsersListApi(token, keyword)

    override suspend fun userLikeVideoListApi(
        token: String,
        type: String

    ): Response<VideoResponseModel> = apiService.userLikeVideoListApi(token, type)

    override suspend fun otherUserProfileApi(
        token: String,
        user_id: String

    ): Response<OtherUserProfileResponse> = apiService.otherUserProfileApi(token, user_id)

    override suspend fun userViewPostApi(
        token: String,
        post_id: String

    ): Response<CommonDataResponse> = apiService.userViewPostApi(token, post_id)

}