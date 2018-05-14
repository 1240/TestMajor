package com.l24o.mad.testmajor.data.network

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager


sealed class BaseClientFactory {

    companion object {

        private const val CONNECT_TIMEOUT_MILLIS = 5_000L
        private const val READ_TIMEOUT_MILLIS = 30_000L
        private const val WRITE_TIMEOUT_MILLIS = 10_000L
    }

    internal fun buildClientWith(connectTimeOut: Long = CONNECT_TIMEOUT_MILLIS,
                                 readTimeOut: Long = READ_TIMEOUT_MILLIS,
                                 writeTimeOut: Long = WRITE_TIMEOUT_MILLIS,
                                 decoration: (OkHttpClient.Builder) -> Unit): OkHttpClient {

        with(OkHttpClient.Builder()) {
            connectTimeout(connectTimeOut, java.util.concurrent.TimeUnit.MILLISECONDS)
            readTimeout(readTimeOut, java.util.concurrent.TimeUnit.MILLISECONDS)
            writeTimeout(writeTimeOut, java.util.concurrent.TimeUnit.MILLISECONDS)

            disableSSL(this)

            decoration.invoke(this)
            return build()
        }
    }

    @SuppressLint("LogNotTimber")
    internal fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @SuppressLint("TrustAllX509TrustManager")
    private fun disableSSL(builder: OkHttpClient.Builder) {

        val sslContext = SSLContext.getInstance("SSL")
        val trustManager: X509TrustManager = object : X509TrustManager {
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                //do nothing
            }

            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                //do nothing
            }

            override fun getAcceptedIssuers(): Array<out X509Certificate>? = arrayOf()
        }
        sslContext.init(null, arrayOf(trustManager), SecureRandom())

        builder.sslSocketFactory(sslContext.socketFactory, trustManager)
        builder.hostnameVerifier { _, _ -> true }
    }
}

class CommonClientFactory : BaseClientFactory() {

    fun createCommon(): OkHttpClient {
        return buildClientWith { builder ->
            with(builder.interceptors()) {
                add(getLoggingInterceptor())
            }
        }
    }
}