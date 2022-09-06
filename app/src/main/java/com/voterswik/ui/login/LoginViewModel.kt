package com.voterswik.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.network.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val loginResponse = MutableLiveData<LoginResponseModel>()
    val forgotResponse = MutableLiveData<LoginResponseModel>()
    val changePasswordResponse = MutableLiveData<LoginResponseModel>()
    val progressBarStatus = MutableLiveData<Boolean>()
    var error = MutableLiveData<Int>()

    fun userLogin(
        type: String,
        username: String,
        password: String,
        device_id: String,
        device_token: String
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {

            val response =
                mainRepository.userLogin(type, username, password, device_id, device_token)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                loginResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }

    }

    fun forgotPassword(
        username: String,
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.sendOtpApi(username)
            if (response.isSuccessful) {

                progressBarStatus.value = false
                Log.d("TAG", "success")
                forgotResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", "Failed")
            }
        }
    }

    fun changePassword(
        newPassword: String,
        userId: String
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.changePassword(newPassword, userId)
            if (response.isSuccessful) {

                progressBarStatus.value = false
                Log.d("TAG", "success")
                changePasswordResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", "Failed")
            }
        }
    }
}