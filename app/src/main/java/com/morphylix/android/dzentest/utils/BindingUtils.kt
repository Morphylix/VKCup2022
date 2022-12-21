package com.morphylix.android.dzentest.utils

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter

private const val TAG = "BindingUtils"

@BindingAdapter("android:src")
fun postImage(view: ImageView, imageResource: Int) {
    val bitmap = BitmapFactory.decodeResource(view.resources, imageResource)
    Log.i(TAG, "img res = $imageResource")
    view.setImageBitmap(bitmap)
}