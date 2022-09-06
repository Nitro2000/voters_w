package com.voterswik.ui.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.model.MusicPlayerResponse
import com.voterswik.network.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    val musicResponse = MutableLiveData<MusicPlayerResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()


    fun musicListApi(
        token: String,

        ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.musicListApi(token)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                musicResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }
}

