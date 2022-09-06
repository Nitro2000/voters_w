package com.voterswik.ui.postvideo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.network.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class VideoPostViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val videoPostResponse = MutableLiveData<VideoPostResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()
    var error = MutableLiveData<Int>()
    var name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var bio = MutableLiveData<String>()
    var gender = MutableLiveData<String>()


    fun videoPostApi(
        token: String,
        video: MultipartBody.Part?,
    ) {

        progressBarStatus.value = true
        viewModelScope.launch {
            val response =
                mainRepository.createVideoApi(
                    token,
                    video!!
                )
            if (response.isSuccessful) {
                progressBarStatus.value = false
                videoPostResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }
}

