package com.l24o.mad.testmajor.data.entity

import com.google.gson.annotations.SerializedName

data class NWImageItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("img")
    val imageUrl: String
)