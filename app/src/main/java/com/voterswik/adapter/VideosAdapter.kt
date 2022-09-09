package com.voterswik.adapter

import android.content.ComponentCallbacks
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.voterswik.BR
import com.voterswik.databinding.RowHomeBinding
import com.voterswik.model.Data
import com.voterswik.model.ExoPlayerItem
import com.voterswik.utils.OnItemClickListener


class VideosAdapter(
    var context: Context,
    private var videos: ArrayList<Data>,
    private var videoPreparedListener: OnVideoPreparedListener,
    private var listener: OnItemClickListener<Data>,

) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    class VideoViewHolder(
        val binding: RowHomeBinding,
        var context: Context,
        private var videoPreparedListener: OnVideoPreparedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var exoPlayer: ExoPlayer
        fun setVideoPath(url: String) {
            exoPlayer = ExoPlayer.Builder(context).build().also {
                binding.playerView.player = it
            }
            exoPlayer.addListener(playbackStateListener(binding))
            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

//            val dataSourceFactory = DefaultDataSource.Factory(context)
            val med = MediaItem.fromUri(Uri.parse(url))
//            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
//                MediaItem.fromUri(Uri.parse(url))
//            )
            exoPlayer.setMediaItem(med)
            exoPlayer.prepare()


            if (absoluteAdapterPosition == 0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }
            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
        }

        private fun playbackStateListener(binding: RowHomeBinding) = object : Player.Listener{
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = RowHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(view, context, videoPreparedListener)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        val model = videos[position]

        holder.binding.setVariable(BR.item, model)
        holder.binding.setVariable(BR.itemClickListener, listener)

        if (model.uservotecount == 1) {
            holder.binding.blurView.visibility = View.GONE
        } else {

            holder.binding.blurView.visibility = View.VISIBLE
        }


        holder.binding.textVideoTitle.text = model.name
        model.video?.let {
            holder.setVideoPath(it)
        }
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }


}

