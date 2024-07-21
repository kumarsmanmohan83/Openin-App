package com.example.openin_app.model

import android.graphics.drawable.Drawable
import java.io.Serializable

data class TopClicksResponse(
    val image:Int?,
    val title:String?,
    val description:String?
): Serializable
