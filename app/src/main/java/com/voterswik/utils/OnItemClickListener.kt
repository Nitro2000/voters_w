package com.voterswik.utils

import android.view.View

interface OnItemClickListener<T> {

    fun onItemClick(view: View, `object`: T)
}