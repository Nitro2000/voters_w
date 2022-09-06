package com.voterswik.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.BR
import com.voterswik.R
import com.voterswik.databinding.SearchListRowBinding
import com.voterswik.ui.BaseModel

class SearchListAdapter<T : BaseModel> internal constructor(
    private val postList: List<String>,
) : RecyclerView.Adapter<SearchListAdapter.AgentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder =
        AgentViewHolder.from(parent)
    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
    }

    private fun getItem(position: Int): String {
        return postList[position]
    }

    override fun getItemCount(): Int = postList.size

    class AgentViewHolder(val binding: SearchListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentProduct: BaseModel) {

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AgentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: SearchListRowBinding = DataBindingUtil
                    .inflate(
                        layoutInflater, R.layout.search_list_row,
                        parent, false
                    )
                return AgentViewHolder(binding)
            }
        }
    }

}