package com.voterswik

import android.view.View

abstract class BaseViewHolder(itemView: View) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    abstract fun onBind(position: Int, holder: BaseViewHolder)
}