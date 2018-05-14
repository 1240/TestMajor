package com.l24o.mad.testmajor.data.network

import com.l24o.mad.testmajor.Constants
import com.l24o.mad.testmajor.data.network.api.AppApiFactory
import com.l24o.mad.testmajor.data.network.api.BaseApiFactory
import com.l24o.mad.testmajor.data.network.api.CommonRetrofitApi

abstract class AbstractApiHolder<Api, in ApiFactory>(
        private val serverUrl: String,
        private val apiFactory: ApiFactory,
        private val clazz: Class<Api>
) where  ApiFactory : BaseApiFactory {

    private var instance: Pair<String, Api>? = null

    abstract fun createApi(apiFactory: ApiFactory, clazz: Class<Api>, apiUrl: String): Api

    fun provide(): Api {
        synchronized(this) {
            val localInstance = createApiInstance(serverUrl)
            return localInstance.second
        }
    }


    private fun createApiInstance(apiUrl: String): Pair<String, Api> {
        var localInstance = instance

        if (localInstance == null || localInstance.first != apiUrl) {
            val api = createApi(apiFactory, clazz, apiUrl)
            localInstance = Pair(apiUrl, api)
            instance = localInstance
        }
        return localInstance
    }
}

interface AppApiProvider {
    fun provide(): CommonRetrofitApi
}

open class AppApiHolder
    : AbstractApiHolder<CommonRetrofitApi, AppApiFactory>(
        Constants.SERVER_URL,
        AppApiFactory(),
        CommonRetrofitApi::class.java), AppApiProvider {

    override fun createApi(apiFactory: AppApiFactory, clazz: Class<CommonRetrofitApi>, apiUrl: String): CommonRetrofitApi {
        return apiFactory.createCommon(CommonRetrofitApi::class.java, apiUrl)
    }
}