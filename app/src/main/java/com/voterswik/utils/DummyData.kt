package com.voterswik.utils

import com.voterswik.model.ChatModel

object DummyData {
    fun getChatList(): ArrayList<ChatModel> {
        val chatList = ArrayList<ChatModel>()
        chatList.add(
            ChatModel(
                "Hi",
                "hy",

            )
        )
        chatList.add(
            ChatModel(
                "How are U",
                "fine",

            )
        )
        chatList.add(
            ChatModel(
                "U",
                "I am also good",

            )
        )

        return chatList

    }
}