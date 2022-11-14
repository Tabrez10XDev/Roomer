package pro.lj.roomer.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) =
    url.let {  Glide.with(view.context).load(url).into(view) }