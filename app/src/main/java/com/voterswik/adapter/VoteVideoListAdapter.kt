package com.voterswik.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.BR
import com.voterswik.R
import com.voterswik.databinding.RowVoteVideoListBinding
import com.voterswik.ui.BaseModel
import com.voterswik.utils.OnItemClickListener

class VoteVideoListAdapter<T : BaseModel> internal constructor(
    private val videoList: List<BaseModel>, val onItemClickListener: OnItemClickListener<T>
) : RecyclerView.Adapter<VoteVideoListAdapter.AgentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder =
        AgentViewHolder.from(parent)


    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.setVariable(BR.itemClickListener, onItemClickListener)
    }

    private fun getItem(position: Int): BaseModel {
        return videoList[position]
    }

    override fun getItemCount(): Int = videoList.size

    class AgentViewHolder(val binding: RowVoteVideoListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentProduct: BaseModel) {

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AgentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: RowVoteVideoListBinding = DataBindingUtil
                    .inflate(
                        layoutInflater, R.layout.row_vote_video_list,
                        parent, false
                    )
                return AgentViewHolder(binding)
            }
        }
    }

}

