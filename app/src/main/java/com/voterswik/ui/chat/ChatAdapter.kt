package com.voterswik.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.BaseViewHolder
import com.voterswik.model.ShareData
import com.voterswik.databinding.RowChatListBinding

class ChatAdapter(
    val context: Context,
    private val chatList: ArrayList<ShareData>

) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RowChatListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val rowChatInBinding: RowChatListBinding) :
        BaseViewHolder(rowChatInBinding.root) {
        override fun onBind(position: Int, holder: BaseViewHolder) {

           val chatModel=chatList[position]
            //rowChatInBinding.tvMessage.text=chatModel.message
            rowChatInBinding.tvOtherMessage.text=chatModel.name

        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.onBind(position, holder)
    }
}
/*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.BR
import com.voterswik.R
import com.voterswik.databinding.RowChatInBinding
import com.voterswik.ui.BaseModel
import com.voterswik.utils.OnItemClickListener

class ChatAdapter<T : BaseModel> internal constructor(
    private val chatList: List<BaseModel>, val onItemClickListener: OnItemClickListener<T>
) : RecyclerView.Adapter<ChatAdapter.AgentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder =
        AgentViewHolder.from(parent)


    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.setVariable(BR.itemClickListener, onItemClickListener)
    }

    private fun getItem(position: Int): BaseModel {
        return chatList[position]
    }

    override fun getItemCount(): Int = chatList.size

    class AgentViewHolder(val binding: RowChatInBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentProduct: BaseModel) {

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AgentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: RowChatInBinding = DataBindingUtil
                    .inflate(
                        layoutInflater, R.layout.row_chat_in,
                        parent, false
                    )
                return AgentViewHolder(binding)
            }
        }
    }

}
*/
