package com.example.pixplore.domain.model

import android.icu.text.ListFormatter.Width

// we are using splashImage object in the way that they are store in json but through this model we are define this object in out own way
data class UnSplashImage(
    val id : String ,
    val imageUrlSmall : String,
    val imageUrlRegular : String,
    val imageUrlRaw : String,
    val photographerName : String,
    val photographerUserName : String,
    val photographerProfileImageUrl : String,
    val photographerProfileLink : String,
    val width: Int,
    val height : Int,
    val description : String?
)