package com.voterswik.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.BR
import com.voterswik.R
import com.voterswik.databinding.RowVideoListBinding
import com.voterswik.ui.BaseModel
import com.voterswik.utils.OnItemClickListener

class VideoListAdapter<T : BaseModel> internal constructor(
    private val videoList: List<BaseModel>, val onItemClickListener: OnItemClickListener<T>
) : RecyclerView.Adapter<VideoListAdapter.AgentViewHolder>() {

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

    class AgentViewHolder(val binding: RowVideoListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentProduct: BaseModel) {

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AgentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: RowVideoListBinding = DataBindingUtil
                    .inflate(
                        layoutInflater, R.layout.row_video_list,
                        parent, false
                    )
                return AgentViewHolder(binding)
            }
        }
    }

}
/*

class VideoListAdapter(
    private val mList: List<ItemsViewModel>,
) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.lineHeart.setImageResource(ItemsViewModel.image)
        holder.lineLike.setImageResource(ItemsViewModel.likeImage)
        holder.tvFavorite.text = ItemsViewModel.text
        holder.tvLikeNumbers.text = ItemsViewModel.likeText

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val lineHeart: ImageView = itemView.findViewById(R.id.lineHeart)
        val lineLike: ImageView = itemView.findViewById(R.id.lineLike)
        val tvFavorite: TextView = itemView.findViewById(R.id.tvFavorite)
        val tvLikeNumbers: TextView = itemView.findViewById(R.id.tvLikeNumbers)
    }
}*/
