package com.l24o.mad.testmajor.data.network.api

import com.l24o.mad.testmajor.data.entity.NWImageItem
import io.reactivex.Single
import retrofit2.http.GET

interface CommonRetrofitApi {

    @GET("androids")
    fun imageList() : Single<List<NWImageItem>>
}