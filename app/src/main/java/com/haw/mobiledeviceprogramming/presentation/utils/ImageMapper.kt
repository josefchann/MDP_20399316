package com.haw.mobiledeviceprogramming.presentation.utils

import com.haw.mobiledeviceprogramming.R

fun mapImageRes(imageKey: String): Int {
    return when (imageKey) {
        "img_sarahlee" -> R.drawable.img_sarahlee
        "img_johndoe" -> R.drawable.img_johndoe
        "img_ibnusina" -> R.drawable.img_ibnusina
        "img_michaelzhang" -> R.drawable.img_michaelzhang
        "img_ameliatan" -> R.drawable.img_ameliatan
        else -> R.drawable.ic_doctor
    }
}
