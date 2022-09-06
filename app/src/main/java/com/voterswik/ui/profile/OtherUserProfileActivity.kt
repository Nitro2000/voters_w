package com.voterswik.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.voterswik.BuildConfig
import com.voterswik.R
import com.voterswik.adapter.OtherUserVideoListAdapter
import com.voterswik.databinding.ActivityOtherUserProfileBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.BaseModel
import com.voterswik.utils.OnItemClickListener
import com.voterswik.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherUserProfileActivity : BaseActivity(), OnItemClickListener<BaseModel> , View.OnClickListener{

    private val viewModel: UserProfileViewModel by viewModels()
    lateinit var binding: ActivityOtherUserProfileBinding
    lateinit var otherUserVideoList: ArrayList<BaseModel>
    lateinit var otherUserVideoListAdapter: OtherUserVideoListAdapter<BaseModel>
    private var name: String? = null
    private var id: String? = null
    private var image: String? = null
    private var bio: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_user_profile)

        binding.ivShare.setOnClickListener(this)
        name = intent.getStringExtra("name")
        id = intent.getStringExtra("id")
        image = intent.getStringExtra("image")
        bio = intent.getStringExtra("bio")


        binding.tvUsername.text = name
        binding.tvBio.text = bio

        Glide.with(this).load(Uri.parse(image))
            .apply(RequestOptions.placeholderOf(R.drawable.ic_person))
            .apply(RequestOptions.errorOf(R.drawable.ic_person))
            .into(binding.userProfileImage)


        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        otherUserVideoList = ArrayList()

        id?.let { viewModel.otherUserProfileApi("Bearer " + userPref.getToken().toString(), it) }
        viewModel.otherUserResponse.observe(this) {
            if (it.status == 1) {
                binding.item = it!!
                otherUserVideoList.clear()
                if (it.data != null){
                    otherUserVideoList.addAll(it.data)
                }

                otherUserVideoListAdapter = OtherUserVideoListAdapter(otherUserVideoList, this)
                binding.recyclerviewVideo.adapter = otherUserVideoListAdapter


            } else {
                toast(it.message)
            }
        }


    }

    override fun onItemClick(view: View, `object`: BaseModel) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivShare ->{
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

        }
    }
}
