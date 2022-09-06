package com.voterswik.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.voterswik.BR
import com.voterswik.R
import com.voterswik.ui.BaseModel
import com.voterswik.utils.OnItemClickListener

class   RecyclerViewAdapter<T : BaseModel>(var list: List<T>, val onItemClickListener: OnItemClickListener<T>)
    : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    var mActivity: Activity? = null
    var mFragment: Fragment? = null
    var mContext: Context? = null

    fun setActivity(activity: Activity) {
        this.mActivity = activity
    }

    fun setFragment(fragment: Fragment) {
        this.mFragment = fragment
    }

    fun setContext(context: Context) {
        this.mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.setVariable(BR.itemClickListener, onItemClickListener)
    }

    class MyViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        var binding: ViewDataBinding
            internal set

        init {
            this.binding = binding
            this.binding.executePendingBindings()
        }
    }

    private fun getItem(position: Int): BaseModel {
        return list[position]
    }

    override fun getItemViewType(position: Int): Int {


            return R.layout.nav_menu_item

    }
}