package com.voterswik.ui.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.network.MainRepository
import com.voterswik.pref.UserPref
import com.voterswik.model.DashboardResponseModel
import com.voterswik.ui.profile.model.CommonDataResponse
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
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.voteApi(token, post_id, vote)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                voteResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
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