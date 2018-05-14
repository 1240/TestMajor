package com.l24o.mad.testmajor.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageItem(
    val id: Int,
    val title: String,
    val imageUrl: String
) : Parcelable