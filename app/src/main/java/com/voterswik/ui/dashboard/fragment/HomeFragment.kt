package com.voterswik.ui.dashboard.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.voterswik.R
import com.voterswik.adapter.VideosAdapter
import com.voterswik.databinding.FragmentHomeBinding
import com.voterswik.model.Data
import com.voterswik.model.ExoPlayerItem
import com.voterswik.ui.BaseFragment
import com.voterswik.ui.BaseModel
import com.voterswik.ui.dashboard.viewmodel.HomeViewModel
import com.voterswik.ui.profile.model.CommonDataResponse
import com.voterswik.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import rx.android.BuildConfig

@AndroidEntryPoint
class HomeFragment : BaseFragment(), OnItemClickListener<Data> {

    lateinit var binding: FragmentHomeBinding

    private val videos = ArrayList<Data>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private lateinit var videoList: java.util.ArrayList<BaseModel>
    private lateinit var videosAdapter: VideosAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var likeStatus = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        userPref.getToken()?.let { viewModel.dashboardApi("Bearer $it") }
//
        videoList = ArrayList()

        viewModel.dashboardResponse.observe(viewLifecycleOwner) { dashboardResponse ->
            Log.e("@@data", dashboardResponse.status.toString())
            if (dashboardResponse.status == 1) {

                videosAdapter = VideosAdapter(
                    requireContext(),
                    videos,
                    object : VideosAdapter.OnVideoPreparedListener {
                        override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                            exoPlayerItems.add(exoPlayerItem)
                        }
                    }, this
                )
                videos.clear()
                videos.addAll(dashboardResponse.data)
                binding.viewPagerVideos.adapter = videosAdapter
                userPref.getToken()?.let {
                    viewModel.userViewPostApi(
                        "Bearer $it",
                        dashboardResponse.data[0].id.toString()
                    )
                }

                binding.viewPagerVideos.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                        if (previousIndex != -1) {
                            val player = exoPlayerItems[previousIndex].exoPlayer
                            player.pause()
                            player.playWhenReady = false

                        }
                        val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                        if (newIndex != -1) {
                            val player = exoPlayerItems[newIndex].exoPlayer
                            player.playWhenReady = true
                            player.play()
                        }
                    }
                })

            } else {
                snackbar(binding.root, dashboardResponse.message!!)
            }
        }

        viewModel.postLikeResponse.observe(
            viewLifecycleOwner,
            object : Observer<CommonDataResponse?> {

                override fun onChanged(likeModel: CommonDataResponse?) {
                    if (likeModel!!.status == 1) {
                        snackbar(binding.root, likeModel.message!!)
                    } else {
                        snackbar(binding.root, likeModel.message!!)
                    }
                    viewModel.postLikeResponse.removeObserver(this)
                }
            })

        viewModel.voteResponse.observe(viewLifecycleOwner) {
            if (it!!.status == 1) {
                snackbar(binding.root, it.message!!)
            } else {
                snackbar(binding.root, it.message!!)
            }
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()

        val index =
            exoPlayerItems.indexOfFirst { it.position == binding.viewPagerVideos.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            //  player.stop()
            player.playWhenReady = false

        }
    }

    override fun onResume() {
        super.onResume()

        val index =
            exoPlayerItems.indexOfFirst { it.position == binding.viewPagerVideos.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer

                player.clearMediaItems()
                player.stop()
            }
        }
    }

    private fun callLikeApi(likeStatus: String, post_id: String) {
        viewModel.likePostApi(
            "Bearer " + userPref.getToken().toString(),
            post_id,  /*data.id.toString(),*/ likeStatus
        )
    }

    override fun onItemClick(view: View, data: Data) {
        when (view.id) {
            R.id.ivFavorites -> {

                val index = videos.indexOf(data)
                if (data.islike == 1) {

                    likeStatus = "2"
                    data.islike = 2
                    videos[index].likes = videos[index].likes!! - 1
                } else if (data.islike == 2 || data.islike == 0) {

                    likeStatus = "1"
                    data.islike = 1
                    videos[index].likes = videos[index].likes!! + 1
                }
                callLikeApi(likeStatus, data.id.toString())
                Log.e("@@", data.id.toString())
                Log.e("likeStatus@@", likeStatus)
                videos.removeAt(index)
                videos.add(index, data)
                videosAdapter.notifyItemChanged(index)

            }

            R.id.ivVote -> {
                var voteStatus = ""

                Handler(Looper.getMainLooper()).postDelayed({
                    val index = videos.indexOf(data)
                    if (data.uservotecount == 1) {

                        voteStatus = "0"
                        data.uservotecount = 0
                        videos[index].allvotescount = videos[index].allvotescount!!
                    } else if (data.uservotecount == 1 || data.uservotecount == 0) {

                        voteStatus = "1"
                        data.uservotecount = 1
                        videos[index].allvotescount = videos[index].allvotescount!! + 1
                    }
                    viewModel.voteApi(
                        "Bearer " + userPref.getToken().toString(),
                        data.id.toString(), voteStatus
                    )
                    videos.removeAt(index)
                    videos.add(index, data)
                    videosAdapter.notifyItemChanged(index)
                }, 5000)

                /*  if (data.uservotecount == 0) {

                      viewModel.voteApi(
                          "Bearer " + userPref.getToken().toString(),
                          data.id.toString(),
                          "1"
                      )

                      val index = videos.indexOf(data)
                      videos[index].allvotescount = videos[index].allvotescount!! + 1

                      videosAdapter.notifyDataSetChanged()
                      Toast.makeText(
                          requireContext(),
                          "Vote Submitted Successfully!!",
                          Toast.LENGTH_SHORT
                      ).show()

                  }*/
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
            R.id.parentRow -> {

            }
            R.id.tvView -> {
                // userPref.getToken()?.let { viewModel.userViewPostApi("Bearer $it", data.id.toString()) }
            }

        }
    }
}