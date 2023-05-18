package com.dungtran.android.core.englishflashcard.utils.extentions

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dungtran.android.core.englishflashcard.R
import java.io.File

@BindingAdapter("loadImageByUrl")
fun ImageView.loadImageByUrl(url:String){
    if (File(url).isFile){
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .into(this)
    }
    else{
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .into(this)
    }

}


@BindingAdapter("setText")
fun TextView.setText(text : Int){
    this.text = "$text"
}


fun View.onClickWithDebounce(debounceTime:Long =1500L, action:() -> Unit){
    this.setOnClickListener(object :View.OnClickListener{
        private var lastClickTime:Long =0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }

    })
}

@BindingAdapter("snapHelper")
fun RecyclerView.snapHelper(snapHelper: LinearSnapHelper) {
    snapHelper.attachToRecyclerView(this)
}
