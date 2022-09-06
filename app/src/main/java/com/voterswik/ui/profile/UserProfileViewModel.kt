package com.voterswik.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.model.OtherUserProfileResponse
import com.voterswik.network.MainRepository
import com.voterswik.ui.profile.model.VideoResponseModel
import com.voterswik.ui.profile.model.ViewProfileResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val viewProfileResponse = MutableLiveData<ViewProfileResponseModel>()
    var videoResponse = MutableLiveData<VideoResponseModel>()
    var likeResponse = MutableLiveData<VideoResponseModel>()
    var voteResponse = MutableLiveData<VideoResponseModel>()
    var otherUserResponse = MutableLiveData<OtherUserProfileResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()

    fun userProfileApi(
        token: String,
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.viewProfile(token)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                viewProfileResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }


    fun videoListApi(
        token: String, type: String
    ) {

        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.userLikeVideoListApi(token, type)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                videoResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }

    fun likeListApi(
        token: String, type: String
    ) {

        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.userLikeVideoListApi(token, type)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                likeResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }

    fun voteListApi(
        token: String, type: String
    ) {

        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.userLikeVideoListApi(token, type)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                voteResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }

    }


    fun otherUserProfileApi(
        token: String, user_id: String
    ) {

        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.otherUserProfileApi(token, user_id)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                otherUserResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }

    }
}