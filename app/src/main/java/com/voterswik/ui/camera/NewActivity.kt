package com.voterswik.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.voterswik.R
import com.voterswik.databinding.ActivityNewBinding
import com.voterswik.ui.postvideo.VideoPostActivity
import com.voterswik.ui.BaseActivity

class NewActivity : BaseActivity(), View.OnClickListener {
    private var exoPlayer: ExoPlayer? = null
    private lateinit var mediaSource: MediaSource
    private lateinit var binding: ActivityNewBinding
    var PREVIEW_SIZE = 800
    private var videoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new)

        binding.ivCross.setOnClickListener(this)
        binding.tvNext.setOnClickListener(this)
        videoPath = intent.getStringExtra("filepath")
        val imgPath = intent.getStringExtra("filePath")
        Log.e("@@", videoPath.toString())

        if (videoPath!!.contains("mp4")) {
            setVideoPath(videoPath!!)
        } else {
            setVideoPath(videoPath!!)
        }

        /* if (imgPath!!.contains("mp4")) {
             val file: File = File(imgPath)
             Picasso.with(this)
                 .load(R.drawable.ic_action_info)
                 .noFade()
                 .noPlaceholder()
                 .resize(PREVIEW_SIZE, PREVIEW_SIZE)
                 .centerCrop().skipMemoryCache()
                 .into(binding.showImage)
         } else {
             val file: File = File(imgPath)
             Picasso.with(this)
                 .load(Uri.fromFile(file))
                 .noFade()
                 .noPlaceholder()
                 .resize(PREVIEW_SIZE, PREVIEW_SIZE)
                 .centerCrop().skipMemoryCache()
                 .into(binding.showImage)
         }*/
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setVideoPath(url: String) {

        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer!!.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(this@NewActivity, "Can't play this video", Toast.LENGTH_SHORT).show()
            }

        })

        binding.playerViewStory.player = exoPlayer
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
            R.id.ivCross -> {
                finish()
            }
            R.id.tv_next -> {
                val intent = Intent(this, VideoPostActivity::class.java)
                intent.putExtra("filepath", videoPath.toString())
                startActivity(intent)
            }
        }
    }

}