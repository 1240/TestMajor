package com.l24o.mad.testmajor.data.network.api

import com.l24o.mad.testmajor.data.network.CommonClientFactory
import com.l24o.mad.testmajor.data.network.GsonFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

sealed class BaseApiFactory {

    internal fun <T> createApi(clazz: Class<T>, apiUrl: String, client: OkHttpClient): T {
        val builder = Retrofit.Builder()
        return builder.baseUrl(apiUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonFactory.networkGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(clazz)

    }

}


class AppApiFactory : BaseApiFactory() {

    fun <T> createCommon(clazz: Class<T>, apiUrl: String): T = createApi(clazz, apiUrl, CommonClientFactory().createCommon())

}