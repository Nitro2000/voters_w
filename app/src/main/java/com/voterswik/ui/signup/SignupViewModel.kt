package com.voterswik.ui.signup

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.network.MainRepository
import com.voterswik.ui.login.LoginResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

val signUpResponse = MutableLiveData<LoginResponseModel>()
    val progressBarStatus = MutableLiveData<Boolean>()
    var countryValue : String= ""

    fun userSignupApi( fullname: String,
                       email: String,
                       mobile: String,
                       password: String,
                       device_id: String,
                       device_token: String) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.userSignupApi(fullname, email, mobile,  password, device_id, device_token)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                signUpResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }


}