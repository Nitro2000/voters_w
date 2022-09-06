package com.voterswik.ui.postvideo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.voterswik.R
import com.voterswik.databinding.ActivityVideoPostBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.dashboard.DashboardActivity
import com.voterswik.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class VideoPostActivity : BaseActivity(), View.OnClickListener {
    private val mVideoPostViewModel: VideoPostViewModel by viewModels()
    lateinit var binding: ActivityVideoPostBinding
    private var exoPlayer: ExoPlayer? = null
    private lateinit var mediaSource: MediaSource
    private var videoPath: String? = null
    private var videoFile: MultipartBody.Part? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_post)

        onClickListener()

        videoPath = intent.getStringExtra("filepath")

        if (videoPath?.contains(".mp4")==true) {
             setVideoPath(videoPath!!)
        } else {
          setVideoPath(videoPath!!)
        }




        val fileVideo = File(videoPath)
        val requestFile: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            fileVideo)
        videoFile= MultipartBody.Part.createFormData("video", fileVideo.name, requestFile)

        mVideoPostViewModel.progressBarStatus.observe(this) {
            if (it == true) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        mVideoPostViewModel.videoPostResponse.observe(this) {
            if (it.status == 1) {
                Toast.makeText(this, "Video Uploaded Successfully!!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                toast(it.message)
            }
        }

    }

    private fun onClickListener() {
        binding.ivBack.setOnClickListener(this)
        binding.btnPost.setOnClickListener(this)
        binding.btnDeletePost.setOnClickListener(this)
    }


    private fun setVideoPath(url: String) {

        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer!!.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(this@VideoPostActivity, "Can't play this video", Toast.LENGTH_SHORT)
                    .show()
            }

        })

        binding.playerView.player = exoPlayer
        exoPlayer!!.seekTo(0)
        exoPlayer!!.repeatMode = Player.REPEAT_MODE_ONE

        val dataSourceFactory = DefaultDataSource.Factory(this)

        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(Uri.parse(url))
        )

        exoPlayer!!.setMediaSource(mediaSource)
        exoPlayer!!.prepare()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnPost -> {
                mVideoPostViewModel.videoPostApi(
                    "Bearer " + userPref.getToken().toString(),
                    videoFile
                )
            }
            R.id.btnDeletePost -> {

            }
        }
    }
}