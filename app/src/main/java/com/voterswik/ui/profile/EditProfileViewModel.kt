package com.voterswik.ui.profile

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.R
import com.voterswik.network.MainRepository
import com.voterswik.ui.login.LoginResponseModel
import com.voterswik.ui.profile.model.ViewProfileResponseModel
import com.voterswik.utils.CommonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

val updateProfileResponse =MutableLiveData<LoginResponseModel>()
    val viewProfileResponse = MutableLiveData<ViewProfileResponseModel>()
    val progressBarStatus = MutableLiveData<Boolean>()
    var error = MutableLiveData<Int>()
    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var bio = MutableLiveData<String>()
    var gender = MutableLiveData<String>()


    fun updateProfileApi(token: String,
                         name: String,
                         bio:String,
                         email: String,
                         gender: String,
                         image: MultipartBody.Part?,
    ) {

        if (TextUtils.isEmpty(name.trim())) {
            error.postValue(R.string.please_enter_name)
            return
        } else if (TextUtils.isEmpty(email.trim())) {
            error.postValue(R.string.please_enter_email_address)
            return
        } else if (!CommonUtils.isValidMail(email.trim())) {
            error.postValue(R.string.please_enter_valid_email_id)
            return
        }  else {

            val userBio: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), bio)
            val userName: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
            val userEmail: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
            val userGender: RequestBody = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                gender
            )

            progressBarStatus.value = true
            viewModelScope.launch {
                val response =
                    mainRepository.updateProfileApi(token, userName, userBio, userEmail, userGender, image!!)
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    updateProfileResponse?.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
            }
        }
        }

    fun userProfileApi( token: String,
    ):LiveData<ViewProfileResponseModel> {
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
  /*  mEditProfileViewModel.viewProfileResponse.observe(this) {

            if (it!!.status == 1) {
                activityEditProfileBinding.item = it?.data


            } else {
                snackbar(it.message!!)
            }*/
       // }
        return viewProfileResponse
    }
}