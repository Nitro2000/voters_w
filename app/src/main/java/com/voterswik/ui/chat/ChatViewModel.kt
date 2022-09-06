package com.voterswik.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voterswik.model.ChatModel
import com.voterswik.network.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    var mChatList = MutableLiveData<ArrayList<ChatModel>>()


}