package com.l24o.mad.testmajor.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonFactory {
    val networkGson: Gson by lazy {
        val gsonBuilder = GsonBuilder()
        // тут можно добавить type adapter'ы
//        gsonBuilder.registerTypeAdapter(java.lang.Boolean::class.java, BooleanDeserializer())


        gsonBuilder.create()
    }
}