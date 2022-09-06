package com.voterswik.ui.chat


import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.voterswik.R
import com.voterswik.model.ShareData
import com.voterswik.databinding.ActivityChatBinding
import com.voterswik.databinding.DialogPhoneCallBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.BaseModel
import com.voterswik.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ChatActivity : BaseActivity(), View.OnClickListener, OnItemClickListener<BaseModel> {
    private lateinit var activityChatBinding: ActivityChatBinding
    private val mChatViewModel:ChatViewModel by viewModels()
//    lateinit var chatList: ArrayList<BaseModel>
//    lateinit var chatListAdapter: ChatAdapter<BaseModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        activityChatBinding.header.ivBack.setOnClickListener(this)
        activityChatBinding.header.ivPhoneCall.setOnClickListener(this)
        activityChatBinding.header.ivVideoCamera.setOnClickListener(this)


//        chatListAdapter = ChatAdapter(chatList, this)
//        activityChatBinding.rvMessage.adapter = chatListAdapter

      /*  mChatViewModel.mChatList.observe(this) {
            activityChatBinding.rvMessage.adapter = ChatAdapter(this, it)
        }*/
        val data = ArrayList<ShareData>()

        for (i in 1..10) {
            data.add(ShareData("Hi"))
        }

        val adapter = ChatAdapter(this, data)
        activityChatBinding.rvMessage.adapter = adapter

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back->{
                onBackPressed()
            }
            R.id.ivPhoneCall ->{
              phoneCallAlert()
            }
            R.id.ivVideoCamera ->{
                phoneCallAlert()
            }

        }
    }

    private fun phoneCallAlert() {
        val cDialog = Dialog(this,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogPhoneCallBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_phone_call,
            null,
            false
        )
        cDialog.setContentView(bindingDialog.root)
        cDialog.setCancelable(false)
        cDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )

        bindingDialog.ivClose.setOnClickListener {
            cDialog!!.dismiss()

        }
        val window = cDialog.window
        val wlp = window?.attributes

        wlp?.gravity  = Gravity.TOP or Gravity.RIGHT
        wlp?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window?.attributes = wlp
        //}

        cDialog.show()
    }

    override fun onItemClick(view: View, `object`: BaseModel) {
        TODO("Not yet implemented")
    }
}