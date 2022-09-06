package com.voterswik.ui.dashboard.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.voterswik.R
import com.voterswik.databinding.FragmentSearchBinding
import com.voterswik.model.MusicPlayer
import com.voterswik.model.SearchData
import com.voterswik.ui.BaseFragment
import com.voterswik.ui.BaseModel
import com.voterswik.ui.chat.ChatActivity
import com.voterswik.ui.dashboard.adapter.SearchListAdapter
import com.voterswik.ui.dashboard.viewmodel.SearchViewModel
import com.voterswik.ui.profile.OtherUserProfileActivity
import com.voterswik.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment(), OnItemClickListener<BaseModel>, View.OnClickListener {

    lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    lateinit var searchListAdapter: SearchListAdapter<BaseModel>
    lateinit var searchList: ArrayList<BaseModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        binding.ivBack.setOnClickListener(this)
        binding.lifecycleOwner = this
        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener {

            job?.cancel()
            job = MainScope().launch {
                delay(500)

                it.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchListApi(
                            "Bearer " + userPref.getToken().toString(),
                            binding.etSearch.text.toString()
                        )
                    }
                }
            }
        }

//        viewModel.musicListApi("Bearer " + userPref.getToken())

        searchList = ArrayList()
        viewModel.searchResponse.observe(viewLifecycleOwner) {
            Log.e("@@data", it.status.toString())
            if (it.status == 1) {
                searchList.clear()
                Log.e("@@@", it.message!!)
                searchList.addAll(it.data)
                searchListAdapter = SearchListAdapter(searchList, this)
                binding.rvSearchList.adapter = searchListAdapter


            } else {
                snackbar(requireView(), it.message!!)
            }
        }

        return binding.root
    }

    override fun onItemClick(view: View, `object`: BaseModel) {
        when(view.id){
            R.id.parentRow ->{
                if (`object` is SearchData) {
                 val intent=   Intent(requireContext(), OtherUserProfileActivity::class.java)
                    intent.putExtra("id",`object`.id.toString())
                    intent.putExtra("name",`object`.name)
                    intent.putExtra("bio",`object`.bio)
                    intent.putExtra("image",`object`.image)
                        startActivity(intent)
                    }
                }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                activity?.onBackPressed()
            }
        }
    }
}