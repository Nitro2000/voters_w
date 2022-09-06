package com.voterswik.ui.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voterswik.model.SearchResponse
import com.voterswik.network.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val mainRepository: MainRepository) :
    ViewModel() {

    val searchResponse = MutableLiveData<SearchResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()


    fun searchListApi(
        token: String,
        keyword: String
    ) {
        progressBarStatus.value = true
        viewModelScope.launch {
            val response = mainRepository.searchListApi(token, keyword)
            if (response.isSuccessful) {
                progressBarStatus.value = false
                searchResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }
        }
    }

}
