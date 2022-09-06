package com.voterswik.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.BR
import com.voterswik.R
import com.voterswik.databinding.RowOtherUserVideoListBinding
import com.voterswik.ui.BaseModel
import com.voterswik.utils.OnItemClickListener

class OtherUserVideoListAdapter<T : BaseModel> internal constructor(
    private val OtherUserVideoList: List<BaseModel>, val onItemClickListener: OnItemClickListener<T>
) : RecyclerView.Adapter<OtherUserVideoListAdapter.AgentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder =
        AgentViewHolder.from(parent)


    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.setVariable(BR.itemClickListener, onItemClickListener)
    }

    private fun getItem(position: Int): BaseModel {
        return OtherUserVideoList[position]
    }

    override fun getItemCount(): Int = OtherUserVideoList.size

    class AgentViewHolder(val binding: RowOtherUserVideoListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentProduct: BaseModel) {

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AgentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: RowOtherUserVideoListBinding = DataBindingUtil
                    .inflate(
                        layoutInflater, R.layout.row_other_user_video_list,
                        parent, false
                    )
                return AgentViewHolder(binding)
            }
        }
    }
}