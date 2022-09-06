package com.voterswik.binding
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.voterswik.R

class DataBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["bind:load_image"], requireAll = false)
        fun loadImage(view: ImageView, imageUrl: String?) {
            if (imageUrl != null && !imageUrl.equals("")) {
                Glide.with(view)
                    .load(imageUrl)
                    .error(R.drawable.image_placeholder)
                    .into(view)
            }
            else
            {
                Glide.with(view)
                    .load(R.drawable.image_placeholder)
                    .into(view)
            }

        }


        @JvmStatic
        @BindingAdapter(value = ["bind:main", "bind:secondText"], requireAll = true)
        fun setBoldString(view: TextView, maintext: String?, sequence: String?) {
            if(maintext!=null) {
                view.text = getBoldText(maintext!!, sequence!!)
            }
        }



        @JvmStatic
        fun getBoldText(text: String, name: String): SpannableStringBuilder {
            var str = SpannableStringBuilder(text)
            str.setSpan(android.text.style.StyleSpan(Typeface.BOLD),
                0, 0 + text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


            str=str.append(" "+name)
            return str
        }


        @JvmStatic
        @BindingAdapter(value = ["bind:mainDetail", "bind:secondTextDetail"], requireAll = true)
        fun setDetailBoldString(view: TextView, maintext: String?, sequence: String?) {
            view.text = getDetailBoldText(maintext!!, sequence!!)
        }



        @JvmStatic
        fun getDetailBoldText(text: String, name: String): SpannableStringBuilder {
            var str = SpannableStringBuilder(text)
            str.setSpan(android.text.style.StyleSpan(Typeface.BOLD),
                0, 0 + text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


            str=str.append(" "+name)
            return str
        }


    }
}