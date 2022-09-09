package com.voterswik.ui.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.network.MainRepository
import com.voterswik.pref.UserPref
import com.voterswik.model.DashboardResponseModel
import com.voterswik.ui.profile.model.CommonDataResponse
import com.voterswik.utils.CommonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainRepository: MainRepository,userPref: UserPref) : ViewModel() {

    val dashboardResponse = MutableLiveData<DashboardResponseModel>()
    val postLikeResponse = MutableLiveData<CommonDataResponse>()
    val voteResponse = MutableLiveData<CommonDataResponse>()
    val userViewPostResponse = MutableLiveData<CommonDataResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()



    fun dashboardApi(
        token: String,
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.dashboardApi(token)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                dashboardResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }

    fun likePostApi(
        token: String, post_id: String, type: String
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.postLikeApi(token, post_id, type)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                postLikeResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }

    fun voteApi(
        token: String, post_id: String, vote: String
    ) {
        if (vote == "1") {
            progressBarStatus.value = true
            viewModelScope.launch {
                val response = mainRepository.voteApi(token, post_id, vote)
                if (response.isSuccessful) {
                    voteResponse.postValue(response.body())
                } else {
                    Log.d("TAG", response.body().toString())
                }
                progressBarStatus.value = false
            }
        } else {
            // This is to reduce api call and still getting the correct message, adjust due to previous code
            voteResponse.postValue(CommonDataResponse(
                status = 1,
                message = "You already given the vote",
                data = "null"
            ))
        }

    }

    fun userViewPostApi(
        token: String, post_id: String
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.userViewPostApi(token, post_id)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                userViewPostResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }
}