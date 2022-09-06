package com.voterswik.ui.camera

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.voterswik.R
import com.voterswik.adapter.MusicAdapter
import com.voterswik.databinding.ActivityMusicBinding
import com.voterswik.model.MusicPlayer
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.BaseModel
import com.voterswik.ui.dashboard.viewmodel.MusicViewModel
import com.voterswik.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class MusicActivity : BaseActivity(), OnItemClickListener<BaseModel>, View.OnClickListener {

    val viewModel: MusicViewModel by viewModels()

    lateinit var binding: ActivityMusicBinding
    lateinit var musicList: ArrayList<BaseModel>
    lateinit var musicListAdapter: MusicAdapter<BaseModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music)

        binding.ivBack.setOnClickListener(this)
        binding.lifecycleOwner = this
        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        viewModel.musicListApi("Bearer " + userPref.getToken())

        musicList = ArrayList()
        viewModel.musicResponse.observe(this) {
            Log.e("@@data", it.status.toString())
            if (it.status == 1) {
                   musicList.clear()
                Log.e("@@@", it.message!!)
                musicList.addAll(it.data)
                musicListAdapter = MusicAdapter(musicList, this)
                binding.recyclerview.adapter = musicListAdapter

            } else {
                snackbar(it.message!!)
            }
        }

    }


    override fun onItemClick(view: View,`object`: BaseModel) {
        when (view.id) {
            R.id.cardViewRoot -> {

                if (`object` is MusicPlayer) {
                    val mediaPlayer = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        setDataSource(`object`.music_url)
                        prepare() // might take long! (for buffering, etc)
                        start()
                    }

            }}
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back ->{
                onBackPressed()
            }
        }
    }
}

