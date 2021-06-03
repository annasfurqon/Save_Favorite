package com.dicoding.mygithub

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FragmentData(
    var username: String? = "",
    var avatar: String? = "",
):Parcelable
