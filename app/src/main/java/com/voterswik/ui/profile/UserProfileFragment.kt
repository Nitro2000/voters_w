package com.voterswik.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.voterswik.BuildConfig
import com.voterswik.R
import com.voterswik.adapter.LikeVideoListAdapter
import com.voterswik.adapter.VideoListAdapter
import com.voterswik.adapter.VoteVideoListAdapter
import com.voterswik.databinding.FragmentUserProfileBinding
import com.voterswik.ui.BaseFragment
import com.voterswik.ui.BaseModel
import com.voterswik.ui.payment.PaymentPayPal
import com.voterswik.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : BaseFragment(), View.OnClickListener, OnItemClickListener<BaseModel> {

    lateinit var binding: FragmentUserProfileBinding

    private val viewModel: UserProfileViewModel by viewModels()
    lateinit var videoList: ArrayList<BaseModel>
    lateinit var videoListAdapter: VideoListAdapter<BaseModel>
    lateinit var likeList: ArrayList<BaseModel>
    lateinit var likeVideoListAdapter: LikeVideoListAdapter<BaseModel>
    lateinit var voteList: ArrayList<BaseModel>
    lateinit var voteVideoListAdapter: VoteVideoListAdapter<BaseModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)

        binding.btnEditProfile.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.btnVideo.setOnClickListener(this)
        binding.btnLike.setOnClickListener(this)
        binding.btnVote.setOnClickListener(this)

        binding.lifecycleOwner = this
        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        userPref.getToken()?.let { viewModel.userProfileApi("Bearer $it") }
        binding.tvUsername.text = userPref.getName()
        binding.tvBio.text = userPref.getBio()
        videoList = ArrayList()
        likeList = ArrayList()
        voteList = ArrayList()

        viewModel.videoResponse.observe(viewLifecycleOwner) {
            if (it!!.status == 1) {
                videoList.clear()
                videoList.addAll(it.data)
                videoListAdapter = VideoListAdapter(videoList, this)
                binding.recyclerviewVideo.adapter = videoListAdapter
                Log.e("@@listSpecialities", videoList.size.toString())
                videoListAdapter.notifyDataSetChanged()

            } else {
                snackbar(requireView(), it.message.toString())
            }
        }
        viewModel.likeResponse.observe(requireActivity()) {
            if (it!!.status == 1) {
                likeList.clear()
                likeList.addAll(it.data)
                likeVideoListAdapter = LikeVideoListAdapter(likeList, this)
                binding.recyclerViewLikes.adapter = likeVideoListAdapter
                Log.e("@@listSpecialities", likeList.size.toString())
                likeVideoListAdapter.notifyDataSetChanged()
            } else {
                snackbar(requireView(), it.message.toString())
            }
        }
        viewModel.voteResponse.observe(requireActivity()) {
            if (it!!.status == 1) {
                voteList.clear()
                voteList.addAll(it.data)
                voteVideoListAdapter = VoteVideoListAdapter(voteList, this)
                binding.recyclerViewVote.adapter = voteVideoListAdapter
                Log.e("@@listSpecialities", voteList.size.toString())
                voteVideoListAdapter.notifyDataSetChanged()
            } else {
                snackbar(requireView(), it.message.toString())
            }
        }

        viewModel.videoListApi("Bearer " + userPref.getToken().toString(), "1")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.paypaltesting.setOnClickListener {
            val intent = Intent(requireContext(), PaymentPayPal::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        viewModel.viewProfileResponse.observe(viewLifecycleOwner) {
            Log.e("@@data", it.status.toString())
            if (it.status == 1) {
                binding.item = it.data!!
                Glide.with(this).load(Uri.parse(it.data?.userinfo?.image))
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person))
                    .apply(RequestOptions.errorOf(R.drawable.ic_person))
                    .into(binding.userProfileImage)

            } else {
                snackbar(binding.root, it.message!!)
            }
        }
        super.onResume()
    }

    override fun onItemClick(view: View, `object`: BaseModel) {

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEditProfile -> {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.backArrow -> {
                activity?.onBackPressed()
            }

            R.id.ivShare -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

            R.id.btnVideo -> {
                binding.recyclerviewVideo.visibility = View.VISIBLE
                binding.recyclerViewLikes.visibility = View.GONE
                binding.recyclerViewVote.visibility = View.GONE
                viewModel.videoListApi("Bearer " + userPref.getToken().toString(), "1")

            }
            R.id.btnLike -> {
                binding.recyclerviewVideo.visibility = View.GONE
                binding.recyclerViewLikes.visibility = View.VISIBLE
                binding.recyclerViewVote.visibility = View.GONE
                viewModel.likeListApi("Bearer " + userPref.getToken().toString(), "2")
            }
            R.id.btnVote -> {
                binding.recyclerviewVideo.visibility = View.GONE
                binding.recyclerViewLikes.visibility = View.GONE
                binding.recyclerViewVote.visibility = View.VISIBLE

                viewModel.voteListApi("Bearer " + userPref.getToken().toString(), "3")
            }


        }
    }

}