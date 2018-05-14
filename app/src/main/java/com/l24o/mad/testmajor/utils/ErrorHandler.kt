package com.l24o.mad.testmajor.utils

import android.util.Log
import com.l24o.mad.testmajor.R
import com.l24o.mad.testmajor.domain.exception.ConvertException
import java.net.*

object ErrorHandler {

    private const val TAG = "ErrorHandler"

    fun processError(
            throwable: Throwable,
            networkConnectionErrorHandler: ((Throwable) -> Boolean)?=null,
            defaultErrorHandler: ((Throwable) -> Boolean)? = null,
            onErrorText: ((String) -> Unit)? = null
    ) {
        Log.e(TAG, "processError", throwable) // todo future user timber for process error to crashlitics and other
        val isHandled = tryToHandle(throwable, networkConnectionErrorHandler, defaultErrorHandler)
        if (!isHandled && onErrorText != null) {
            showErrorMessage(throwable, onErrorText)
        }
    }

    private fun tryToHandle(
            throwable: Throwable,
            networkConnectionErrorHandler: ((Throwable) -> Boolean)?,
            defaultErrorHandler: ((Throwable) -> Boolean)?
    ): Boolean {
        val isNetworkHandled = isNetworkExceptionHandled(throwable, networkConnectionErrorHandler)
        val isAnyExceptionHandled = isAnyExceptionHandled(throwable, defaultErrorHandler)
        return isNetworkHandled || isAnyExceptionHandled
    }

    private fun isNetworkExceptionHandled(
            throwable: Throwable,
            handler: ((Throwable) -> Boolean)?
    ): Boolean {
        return isNetworkException(throwable)
                && handler?.invoke(throwable) ?: false
    }


    private fun isAnyExceptionHandled(
            throwable: Throwable,
            handler: ((Throwable) -> Boolean)?
    ): Boolean {
        return handler?.invoke(throwable) ?: false
    }

    private fun showErrorMessage(throwable: Throwable, onErrorText: ((String) -> Unit)) {
        val message = when {
            isNetworkException(throwable) -> ResourceManager.getString(R.string.error_no_internet)
            throwable is ConvertException -> ResourceManager.getString(R.string.error_convert_exception)
            else -> ResourceManager.getString(R.string.error_unknown)
        }
        onErrorText.invoke(message)
    }


    fun isNetworkException(error: Throwable?): Boolean {
        return when (error) {
            is ConnectException,
            is SocketException,
            is SocketTimeoutException,
            is UnknownHostException,
            is ProtocolException -> true
            else -> false
        }
    }
}